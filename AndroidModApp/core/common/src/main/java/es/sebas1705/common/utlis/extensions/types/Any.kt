package es.sebas1705.common.utlis.extensions.types

import android.util.Log

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
fun Any.logI(message: String) = Log.i(this::class.java.simpleName, message)

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
fun Any.logE(message: String) = Log.e(this::class.java.simpleName, message)

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
fun Any.logD(message: String) = Log.d(this::class.java.simpleName, message)

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
fun Any.logV(message: String) = Log.v(this::class.java.simpleName, message)

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
fun Any.logW(message: String) = Log.w(this::class.java.simpleName, message)

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
fun Any.logWTF(message: String) = Log.wtf(this::class.java.simpleName, message)
