package es.sebas1705.models

enum class DarkThemePreference(val value: Int) {
    System(0),
    Light(1),
    Dark(2);

    companion object {
        fun fromValue(value: Int) = entries.firstOrNull { it.value == value } ?: System
    }
}
