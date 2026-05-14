package es.sebas1705.analytics.datasources

import com.google.firebase.analytics.FirebaseAnalytics
import es.sebas1705.analytics.config.UserProperty
import javax.inject.Inject

/**
 * Data source for setting user properties in Firebase Analytics.
 *
 * @property firebaseAnalytics [FirebaseAnalytics]: Instance of Firebase Analytics.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class UserPropertiesDataSource @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    /**
     * Set a user property in Firebase Analytics.
     *
     * @param property [UserProperty]: The user property to set.
     * @param value [String]: The value to set for the user property.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun setUserProperty(property: UserProperty, value: String) {
        firebaseAnalytics.setUserProperty(property.tag, value)
    }
}