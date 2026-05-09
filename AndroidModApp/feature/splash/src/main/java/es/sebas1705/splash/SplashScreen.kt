package es.sebas1705.splash

import androidx.compose.runtime.Composable
import es.sebas1705.splash.design.SplashDesign
import androidx.compose.ui.Modifier

/**
 * Splash Screen of the application
 *
 * @author Sebastian Ramiro Entrerrios García
 * @since 0.1.0
 */
@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    SplashDesign(modifier)
}