package es.sebas1705.firestore.usage

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

/**
 * Tracks Internet-mode session consumption to stay within Firebase free-tier limits.
 *
 * A single Firestore document (`config/internet_usage`) stores:
 *  - `date`              – current UTC date (YYYY-MM-DD), used as a daily-reset key.
 *  - `sessionsToday`     – sessions created on [date].
 *  - `maxSessionsPerDay` – operator-controlled limit. Change it directly in the Firebase
 *                          Console without shipping an app update. Defaults to [DEFAULT_MAX_PER_DAY].
 *
 * [tryClaimSession] runs an atomic Firestore transaction:
 *  1. Read the document.
 *  2. If `date` differs from today → treat `sessionsToday` as 0 (new day).
 *  3. Compare against the limit; throw [SessionLimitReachedException] if over.
 *  4. Persist the incremented counter.
 *
 * Cost: 1 read + 1 write per successful room creation — negligible against the free tier.
 */
class InternetUsageDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    private val usageDoc get() = firestore.collection(COLLECTION).document(DOCUMENT)

    /**
     * Atomically claims one Internet session slot for today.
     *
     * @param maxPerDay Fallback limit used only when the Firestore document does not yet
     *   contain [FIELD_MAX_PER_DAY]. The operator can override this at any time via the
     *   Firebase Console.
     * @return [Result.success] if a slot was reserved.
     * @return [Result.failure] wrapping [SessionLimitReachedException] when the daily cap
     *   is reached, or a generic exception on network/Firestore errors.
     */
    suspend fun tryClaimSession(maxPerDay: Int = DEFAULT_MAX_PER_DAY): Result<Unit> =
        runCatching {
            val today = todayUtc()
            firestore.runTransaction { tx ->
                val snap = tx.get(usageDoc)
                val storedDate = snap.getString(FIELD_DATE) ?: ""
                val isNewDay = storedDate != today

                // On a new day reset the counter; otherwise use the stored value.
                val sessions = if (isNewDay) 0
                else (snap.getLong(FIELD_SESSIONS_TODAY) ?: 0L).toInt()

                // Respect an operator-set limit from Firestore; fall back to the in-app default.
                val limit = (snap.getLong(FIELD_MAX_PER_DAY) ?: maxPerDay.toLong()).toInt()

                if (sessions >= limit) {
                    throw SessionLimitReachedException(sessions, limit)
                }

                tx.set(
                    usageDoc,
                    mapOf(
                        FIELD_DATE to today,
                        FIELD_SESSIONS_TODAY to (sessions + 1).toLong(),
                        FIELD_MAX_PER_DAY to limit.toLong(),
                    ),
                    SetOptions.merge(),
                )
            }.await()
        }

    // ── Helpers ────────────────────────────────────────────────────────────

    private fun todayUtc(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US)
            .also { it.timeZone = TimeZone.getTimeZone("UTC") }
            .format(Date())

    // ── Exceptions ─────────────────────────────────────────────────────────

    /**
     * Thrown when the daily Internet session limit has been reached.
     * The message is intended to be shown directly to the user.
     */
    class SessionLimitReachedException(val current: Int, val max: Int) : Exception(
        "Daily Internet game limit reached ($current/$max). " +
            "Try again tomorrow or switch to Local Network mode."
    )

    // ── Constants ──────────────────────────────────────────────────────────

    companion object {
        /** Default daily cap when no override is stored in Firestore. */
        const val DEFAULT_MAX_PER_DAY = 50

        private const val COLLECTION = "config"
        private const val DOCUMENT = "internet_usage"
        private const val FIELD_DATE = "date"
        private const val FIELD_SESSIONS_TODAY = "sessionsToday"
        private const val FIELD_MAX_PER_DAY = "maxSessionsPerDay"
    }
}
