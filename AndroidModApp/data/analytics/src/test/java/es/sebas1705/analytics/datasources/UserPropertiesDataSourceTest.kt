package es.sebas1705.analytics.datasources

import com.google.firebase.analytics.FirebaseAnalytics
import es.sebas1705.analytics.config.UserProperty
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify

class UserPropertiesDataSourceTest {

    @Mock
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var userPropertiesDataSource: UserPropertiesDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userPropertiesDataSource = UserPropertiesDataSource(firebaseAnalytics)
    }

    @Test
    fun `setUserProperty calls firebaseAnalytics setUserProperty with correct tag and value`() {
        val property = UserProperty.PlayerLevel
        val value = "10"

        userPropertiesDataSource.setUserProperty(property, value)

        verify(firebaseAnalytics).setUserProperty(eq(property.tag), eq(value))
    }

    @Test
    fun `setUserProperty supports empty value`() {
        val property = UserProperty.PlayerLevel
        val value = ""

        userPropertiesDataSource.setUserProperty(property, value)

        verify(firebaseAnalytics).setUserProperty(eq(property.tag), eq(value))
    }
}
