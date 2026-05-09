package es.sebas1705.authentication.config

import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsAuthTest {

    @Test
    fun `constants keep expected defaults`() {
        assertEquals(false, SettingsAuth.FILTER_BY_AUTHORIZED_ACCOUNTS)
        assertEquals(
            "An error occurred on authentication by an exception",
            SettingsAuth.ERROR_GENERIC_MESSAGE_EX
        )
        assertEquals(
            "An error occurred on authentication by failure listener",
            SettingsAuth.ERROR_GENERIC_MESSAGE_FAIL
        )
        assertEquals(
            "Not correctly logged user to take their data",
            SettingsAuth.NOT_LOGGED_USER
        )
        assertEquals("User not logged out", SettingsAuth.USER_NOT_OUT)
        assertEquals("Wrong credentials", SettingsAuth.WRONG_CREDENTIALS)
    }
}

