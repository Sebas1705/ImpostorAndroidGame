package es.sebas1705.splash.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

internal data class SplashBrandingData(
    @param:DrawableRes val logoDrawableRes: Int,
    @param:StringRes val logoContentDescriptionRes: Int
)


