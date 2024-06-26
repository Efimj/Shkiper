package com.efim.shkiper.core.helper

import androidx.annotation.Keep

@Keep
class NumberHelper {
    fun formatNumber(number: ULong): String {
        val suffixes = listOf("", "K", "M", "B", "T")

        var num = number.toDouble()
        var suffixIndex = 0

        while (num >= 1000 && suffixIndex < suffixes.size - 1) {
            num /= 1000
            suffixIndex++
        }

        return buildString {
            append(num.toInt())

            if (suffixIndex > 0) {
                append(suffixes[suffixIndex])
            }
        }
    }
}