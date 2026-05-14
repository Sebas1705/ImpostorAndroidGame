package es.sebas1705.common.responses


/**
 * Sealed class to represent the type of the error response
 *
 * @param tag [String]: Tag of the error
 *
 * @property BAD_REQUEST [ErrorResponseType]: Bad request error
 * @property INTERNAL [ErrorResponseType]: Internal error
 * @property NOT_FOUND [ErrorResponseType]: Not found error
 * @property UNAUTHORIZED [ErrorResponseType]: Unauthorized error
 * @property FORBIDDEN [ErrorResponseType]: Forbidden error
 * @property CONFLICT [ErrorResponseType]: Conflict error
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
enum class ErrorResponseType(val tag: String) {
    BAD_REQUEST("Bad request error"),
    INTERNAL("Internal error"),
    NOT_FOUND("Not found error"),
    UNAUTHORIZED("Unauthorized error"),
    FORBIDDEN("Forbidden error"),
    CONFLICT("Conflict error");
}