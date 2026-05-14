package es.sebas1705.common.managers

/**
 * Abstract class to represent the class that logs the event
 *
 * @property packageName [String]: Name of the package where the class is located
 * @property className [String]: Name of the class that logs the event
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
abstract class ClassLogData {
    val packageName: String = this::class.java.packageName
    val className: String = this::class.simpleName ?: "UnknownClass"
}