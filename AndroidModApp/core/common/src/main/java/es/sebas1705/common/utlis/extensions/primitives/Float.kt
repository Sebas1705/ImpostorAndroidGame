package es.sebas1705.common.utlis.extensions.primitives

import android.annotation.SuppressLint

/**
 * Format a float number to a string with two decimal and a percentage symbol
 *
 * @receiver [Float]: the number to format
 *
 * @return [String]: the number formatted
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@SuppressLint("DefaultLocale")
fun Float.percentageFormat(): String = String.format("%.2f", this * 100) + "%"