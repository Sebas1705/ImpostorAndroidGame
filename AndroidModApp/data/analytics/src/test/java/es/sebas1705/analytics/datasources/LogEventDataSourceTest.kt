package es.sebas1705.analytics.datasources

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import es.sebas1705.analytics.config.EventLog
import es.sebas1705.analytics.model.AnalyticsModel
import es.sebas1705.common.managers.ClassLogData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class LogEventDataSourceTest {

    private class ExampleLogData : ClassLogData()

    @Mock
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var logEventDataSource: LogEventDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        logEventDataSource = LogEventDataSource(firebaseAnalytics)
    }

    @Test
    fun `logEvent calls firebaseAnalytics logEvent with correct tag and bundle`() {
        val event = EventLog.SignIn
        val bundle = Bundle()

        logEventDataSource.logEvent(event, bundle)

        // This uses the overload logEvent(String, Bundle)
        verify(firebaseAnalytics).logEvent(event.tag, bundle)
    }

    @Test
    fun `logError calls firebaseAnalytics logEvent with error tag and correct params`() {
        val classLogData = ExampleLogData()
        val error = "Test Error Message"

        logEventDataSource.logError(classLogData, error)

        // The KTX builder ends up calling the overload logEvent(String, Bundle)
        val nameCaptor = argumentCaptor<String>()
        val bundleCaptor = argumentCaptor<Bundle>()
        verify(firebaseAnalytics).logEvent(nameCaptor.capture(), bundleCaptor.capture())

        assertEquals(EventLog.Error.tag, nameCaptor.firstValue)

        val capturedBundle = bundleCaptor.firstValue
        assertEquals(classLogData.packageName, capturedBundle.getString("package"))
        assertEquals(classLogData.className, capturedBundle.getString("class"))
        assertEquals(error, capturedBundle.getString("error"))
    }

    @Test
    fun `sendEvent calls firebaseAnalytics logEvent with model title and all params`() {
        val bundle1 = Bundle().apply { putString("inner", "value") }
        val bundleArray = arrayOf(Bundle().apply { putInt("arr", 1) })

        val model = AnalyticsModel(
            title = "test_event",
            analyticsString = listOf("s_key" to "s_val"),
            analyticsLong = listOf("l_key" to 1L),
            analyticsDouble = listOf("d_key" to 1.0),
            analyticsBundle = listOf("b_key" to bundle1),
            analyticsBundleArray = listOf("ba_key" to bundleArray)
        )

        logEventDataSource.sendEvent(model)

        val nameCaptor = argumentCaptor<String>()
        val bundleCaptor = argumentCaptor<Bundle>()
        verify(firebaseAnalytics).logEvent(nameCaptor.capture(), bundleCaptor.capture())

        assertEquals(model.title, nameCaptor.firstValue)

        val capturedBundle = bundleCaptor.firstValue
        assertEquals("s_val", capturedBundle.getString("s_key"))
        assertEquals(1L, capturedBundle.getLong("l_key"))
        assertEquals(1.0, capturedBundle.getDouble("d_key"), 0.0)

        val nested = capturedBundle.getBundle("b_key")
        assertNotNull(nested)
        assertEquals("value", nested?.getString("inner"))

        val parcelables = capturedBundle.getParcelableArray("ba_key")
        assertNotNull(parcelables)
        val parcelablesNotNull = requireNotNull(parcelables)
        assertEquals(1, parcelablesNotNull.size)

        val first = parcelablesNotNull[0]
        assertNotNull(first)
        val firstBundle = first as Bundle
        assertEquals(1, firstBundle.getInt("arr"))
    }

    @Test
    fun `sendEvent with empty params still logs event with empty bundle`() {
        val model = AnalyticsModel(title = "empty_event")

        logEventDataSource.sendEvent(model)

        val nameCaptor = argumentCaptor<String>()
        val bundleCaptor = argumentCaptor<Bundle>()
        verify(firebaseAnalytics).logEvent(nameCaptor.capture(), bundleCaptor.capture())

        assertEquals(model.title, nameCaptor.firstValue)
        // Should be a non-null bundle but with no known keys
        val capturedBundle = bundleCaptor.firstValue
        assertEquals(0, capturedBundle.keySet().size)
    }

    @Test
    fun `sendEvent supports multiple entries per type`() {
        val model = AnalyticsModel(
            title = "multi_event",
            analyticsString = listOf("k1" to "v1", "k2" to "v2"),
            analyticsLong = listOf("l1" to 1L, "l2" to 2L),
            analyticsDouble = listOf("d1" to 1.5, "d2" to 2.5),
        )

        logEventDataSource.sendEvent(model)

        val nameCaptor = argumentCaptor<String>()
        val bundleCaptor = argumentCaptor<Bundle>()
        verify(firebaseAnalytics).logEvent(nameCaptor.capture(), bundleCaptor.capture())

        assertEquals(model.title, nameCaptor.firstValue)
        val b = bundleCaptor.firstValue
        assertEquals("v1", b.getString("k1"))
        assertEquals("v2", b.getString("k2"))
        assertEquals(1L, b.getLong("l1"))
        assertEquals(2L, b.getLong("l2"))
        assertEquals(1.5, b.getDouble("d1"), 0.0)
        assertEquals(2.5, b.getDouble("d2"), 0.0)
    }

    @Test
    fun `sendEvent supports bundle arrays with multiple items`() {
        val bundleArray = arrayOf(
            Bundle().apply { putInt("arr", 1) },
            Bundle().apply { putInt("arr", 2) },
        )
        val model = AnalyticsModel(
            title = "bundle_array_event",
            analyticsBundleArray = listOf("ba_key" to bundleArray)
        )

        logEventDataSource.sendEvent(model)

        val nameCaptor = argumentCaptor<String>()
        val bundleCaptor = argumentCaptor<Bundle>()
        verify(firebaseAnalytics).logEvent(nameCaptor.capture(), bundleCaptor.capture())

        assertEquals(model.title, nameCaptor.firstValue)
        val parcelables = bundleCaptor.firstValue.getParcelableArray("ba_key")
        assertNotNull(parcelables)
        val items = requireNotNull(parcelables)
        assertEquals(2, items.size)
        assertEquals(1, (items[0] as Bundle).getInt("arr"))
        assertEquals(2, (items[1] as Bundle).getInt("arr"))
    }

    @Test
    fun `logError supports empty error message`() {
        val classLogData = ExampleLogData()
        val error = ""

        logEventDataSource.logError(classLogData, error)

        val nameCaptor = argumentCaptor<String>()
        val bundleCaptor = argumentCaptor<Bundle>()
        verify(firebaseAnalytics).logEvent(nameCaptor.capture(), bundleCaptor.capture())

        assertEquals(EventLog.Error.tag, nameCaptor.firstValue)
        assertEquals("", bundleCaptor.firstValue.getString("error"))
    }
}
