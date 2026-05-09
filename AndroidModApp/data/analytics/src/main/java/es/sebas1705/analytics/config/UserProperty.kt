package es.sebas1705.analytics.config

/**
 * Sealed class to represent the user properties
 *
 * @param tag [String]: Tag of the user property
 *
 * @property FavoriteCategory [UserProperty]: Favorite category user property
 * @property FavoriteDifficulty [UserProperty]: Favorite difficulty user property
 * @property FavoriteType [UserProperty]: Favorite type user property
 * @property PlayerLevel [UserProperty]: Player level user property
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
sealed class UserProperty(val tag: String) {
    data object FavoriteCategory : UserProperty("favorite_category")
    data object FavoriteDifficulty : UserProperty("favorite_difficulty")
    data object FavoriteType : UserProperty("favorite_type")
    data object PlayerLevel : UserProperty("player_level")
}