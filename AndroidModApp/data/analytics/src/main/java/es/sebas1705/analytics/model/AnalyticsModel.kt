package es.sebas1705.analytics.model

import android.os.Bundle

/**
 * Model to represent an analytics
 *
 * @property title [String]: Title of the analytics
 * @property analyticsString [List]<[Pair]<[String],[String]>>: List of analytics with String values
 * @property analyticsLong [List]<[Pair]<[String],[Long]>>: List of analytics with Long values
 * @property analyticsDouble [List]<[Pair]<[String],[Double]>>: List of analytics with Double values
 * @property analyticsBundle [List]<[Pair]<[String],[Bundle]>>: List of analytics with Bundle values
 * @property analyticsBundleArray [List]<[Pair]<[String],[Array]<[Bundle]>>>: List of analytics with Array of Bundle values
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
data class AnalyticsModel(
    val title: String,
    val analyticsString: List<Pair<String, String>> = emptyList(),
    val analyticsLong: List<Pair<String, Long>> = emptyList(),
    val analyticsDouble: List<Pair<String, Double>> = emptyList(),
    val analyticsBundle: List<Pair<String, Bundle>> = emptyList(),
    val analyticsBundleArray: List<Pair<String, Array<Bundle>>> = emptyList()
)
