package es.sebas1705.common.utlis.extensions.types

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log

@SuppressLint("DiscouragedPrivateApi", "PrivateApi", "ObsoleteSdkInt")
private object LogVariantGate {
	val isDevVariant: Boolean by lazy {
		val processName = runCatching {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
				Application.getProcessName()
			} else {
				Class.forName("android.app.ActivityThread")
					.getDeclaredMethod("currentProcessName")
					.invoke(null) as? String
			}
		}.getOrNull().orEmpty()

		processName.contains(".dev", ignoreCase = true)
	}
}

private const val LOG_MESSAGE_CHUNK_SIZE = 3500
private const val UNKNOWN_CALLER = "UnknownCaller"
private const val UNKNOWN_DOMAIN = "GEN"

private inline fun logIfDev(
	tag: String,
	level: String,
	levelIcon: String,
	message: String,
	logger: (String, String) -> Int
) {
	if (!LogVariantGate.isDevVariant) return

	val decoratedMessage = decorateLogMessage(
		message = message,
		level = level,
		levelIcon = levelIcon
	)
	val chunks = chunkMessage(decoratedMessage)
	if (chunks.size == 1) {
		logger(tag, chunks.first())
		return
	}

	chunks.forEachIndexed { index, chunk ->
		logger(tag, "(${index + 1}/${chunks.size}) $chunk")
	}
}

private val Any.tag: String
	get() = this as? String ?: this::class.java.simpleName

@Suppress("CyclomaticComplexMethod")
private fun decorateLogMessage(
	message: String,
	level: String,
	levelIcon: String
): String {
	val threadName = Thread.currentThread().name
	val caller = resolveCaller()
	val domain = when (caller.className) {
		null -> UNKNOWN_DOMAIN
		else -> when {
			caller.className.contains(".main.") || caller.className.contains(".nav.") -> "NAV"
			caller.className.contains(".offlinegame.") -> "GAME"
			caller.className.contains(".couchbase.") || caller.className.contains(".room.") || caller.className.contains(".repositories.") -> "DB"
			caller.className.contains(".authentication.") || caller.className.contains(".login.") -> "AUTH"
			caller.className.contains(".mvi.") || caller.className.contains("ViewModel") -> "MVI"
			caller.className.contains(".network.") || caller.className.contains(".retrofit.") -> "NET"
			else -> UNKNOWN_DOMAIN
		}
	}
	val callerLabel = caller.fileName?.let { "$it:${caller.lineNumber}" } ?: UNKNOWN_CALLER
	return "[$level$levelIcon][$domain][$threadName][$callerLabel] $message"
}

private fun resolveCaller(): StackTraceElement = runCatching {
	Thread.currentThread()
		.stackTrace
		.firstOrNull { element ->
			val className = element.className
			!className.contains("AnyKt") &&
				!className.startsWith("java.lang.Thread") &&
				!className.startsWith("dalvik.system")
		}
}.getOrNull() ?: StackTraceElement("", "", UNKNOWN_CALLER, -1)

private fun chunkMessage(message: String): List<String> {
	if (message.length <= LOG_MESSAGE_CHUNK_SIZE) return listOf(message)
	val chunks = mutableListOf<String>()
	var start = 0
	while (start < message.length) {
		val end = (start + LOG_MESSAGE_CHUNK_SIZE).coerceAtMost(message.length)
		chunks.add(message.substring(start, end))
		start = end
	}
	return chunks
}

/**
 * Extension function to log a message with the INFO level.
 *
 * @param message The message to log.
 *
 * @receiver Any The instance on which the function is called.
 *
 * @since 0.1.0
 * @author Sebas1705 22/07/2025
 */
fun Any.logI(message: String) = logIfDev(this.tag, "I", "*", message, Log::i)

/**
 * Extension function to log a message with the ERROR level.
 *
 * @param message The message to log.
 *
 * @receiver Any The instance on which the function is called.
 *
 * @since 0.1.0
 * @author Sebas1705 22/07/2025
 */
fun Any.logE(message: String, throwable: Throwable? = null) {
	val resolvedMessage = if (throwable == null) {
		message
	} else {
		"$message | cause=${throwable::class.java.simpleName}: ${throwable.message}\n" +
			Log.getStackTraceString(throwable)
	}
	logIfDev(this.tag, "E", "!", resolvedMessage, Log::e)
}

/**
 * Extension function to log a message with the DEBUG level.
 *
 * @param message The message to log.
 *
 * @receiver Any The instance on which the function is called.
 *
 * @since 0.1.0
 * @author Sebas1705 22/07/2025
 */
fun Any.logD(message: String) = logIfDev(this.tag, "D", ">", message, Log::d)

/**
 * Extension function to log a message with the VERBOSE level.
 *
 * @param message The message to log.
 *
 * @receiver Any The instance on which the function is called.
 *
 * @since 0.1.0
 * @author Sebas1705 22/07/2025
 */
fun Any.logV(message: String) = logIfDev(this.tag, "V", "~", message, Log::v)

/**
 * Extension function to log a message with the WARN level.
 *
 * @param message The message to log.
 *
 * @receiver Any The instance on which the function is called.
 *
 * @since 0.1.0
 * @author Sebas1705 22/07/2025
 */
fun Any.logW(message: String) = logIfDev(this.tag, "W", "?", message, Log::w)

/**
 * Extension function to log a message with the WTF level.
 *
 * @param message The message to log.
 *
 * @receiver Any The instance on which the function is called.
 *
 * @since 0.1.0
 * @author Sebas1705 22/07/2025
 */
fun Any.logWTF(message: String) = logIfDev(this.tag, "A", "#", message, Log::wtf)
