package es.sebas1705.common.extensions.types

import android.util.Log
import es.sebas1705.common.utlis.extensions.types.logD
import es.sebas1705.common.utlis.extensions.types.logE
import es.sebas1705.common.utlis.extensions.types.logI
import es.sebas1705.common.utlis.extensions.types.logV
import es.sebas1705.common.utlis.extensions.types.logW
import es.sebas1705.common.utlis.extensions.types.logWTF
import org.junit.Test
import org.mockito.Mockito.mockStatic

class AnyExtensionsTest {

    @Test fun `logI logs message with INFO level`() {
        val instance = Any()
        mockStatic(Log::class.java).use { logMock ->
            instance.logI("Info message")
            logMock.verify { Log.i(instance::class.java.simpleName, "Info message") }
        }
    }

    @Test fun `logE logs message with ERROR level`() {
        val instance = Any()
        mockStatic(Log::class.java).use { logMock ->
            instance.logE("Error message")
            logMock.verify { Log.e(instance::class.java.simpleName, "Error message") }
        }
    }

    @Test fun `logD logs message with DEBUG level`() {
        val instance = Any()
        mockStatic(Log::class.java).use { logMock ->
            instance.logD("Debug message")
            logMock.verify { Log.d(instance::class.java.simpleName, "Debug message") }
        }
    }

    @Test fun `logV logs message with VERBOSE level`() {
        val instance = Any()
        mockStatic(Log::class.java).use { logMock ->
            instance.logV("Verbose message")
            logMock.verify { Log.v(instance::class.java.simpleName, "Verbose message") }
        }
    }

    @Test fun `logW logs message with WARN level`() {
        val instance = Any()
        mockStatic(Log::class.java).use { logMock ->
            instance.logW("Warn message")
            logMock.verify { Log.w(instance::class.java.simpleName, "Warn message") }
        }
    }

    @Test fun `logWTF logs message with WTF level`() {
        val instance = Any()
        mockStatic(Log::class.java).use { logMock ->
            instance.logWTF("WTF message")
            logMock.verify { Log.wtf(instance::class.java.simpleName, "WTF message") }
        }
    }
}
