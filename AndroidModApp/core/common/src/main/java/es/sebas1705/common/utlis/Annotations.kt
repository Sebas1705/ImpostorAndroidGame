package es.sebas1705.common.utlis

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Settings for the preview
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
object PreviewSettings {
    const val WIDTH = 395
    const val HEIGHT = 855
}

/**
 * Annotation to create a preview with the different UI modes
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Preview(
    showBackground = true,
    name = "Day",
    showSystemUi = false,
    locale = "en",
    widthDp = PreviewSettings.WIDTH,
    heightDp = PreviewSettings.HEIGHT
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Night",
    showSystemUi = false,
    locale = "es",
    widthDp = PreviewSettings.WIDTH,
    heightDp = PreviewSettings.HEIGHT
)
annotation class UiModePreviews

/**
 * Annotation to create a preview with the different locales
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Preview(
    showBackground = true,
    name = "Day-en",
    showSystemUi = true,
    locale = "en",
    widthDp = PreviewSettings.WIDTH,
    heightDp = PreviewSettings.HEIGHT
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Night-en",
    showSystemUi = true,
    locale = "en",
    widthDp = PreviewSettings.WIDTH,
    heightDp = PreviewSettings.HEIGHT
)
@Preview(
    showBackground = true,
    name = "Day-es",
    showSystemUi = true,
    locale = "es",
    widthDp = PreviewSettings.WIDTH,
    heightDp = PreviewSettings.HEIGHT
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Night-es",
    showSystemUi = true,
    locale = "es",
    widthDp = PreviewSettings.WIDTH,
    heightDp = PreviewSettings.HEIGHT
)
annotation class LocalePreviews

/**
 * Annotation to create a preview for composable functions
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Preview(name = "Day")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Night")
annotation class ComposablePreview

