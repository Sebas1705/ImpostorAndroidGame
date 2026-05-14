package es.sebas1705.models

enum class AppLanguage(
    val code: String,
    val displayName: String
) {
    Spanish(code = "es", displayName = "Espanol"),
    English(code = "en", displayName = "English");

    companion object {
        fun fromCode(code: String): AppLanguage =
            entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: English
    }
}

