package com.jobik.shkiper.helpers

import androidx.annotation.Keep

@Keep
class TextHelper {
    companion object{
        fun removeMarkdownStyles(input: String): String {
            // Remove emphasis (italics, strikethrough and bold)
            var output = input.replace(Regex("[*]{1,2}|[_]{1,2}|[~]{1,2}"), "")

            // Remove inline code
            output = output.replace(Regex("`{1,2}"), "")

            // Remove headers
            output = output.replace(Regex("^#{1,6}\\s"), "")

            // Remove unordered list indicators
            output = output.replace(Regex("^\\s*[-+*]\\s"), "")

            // Remove ordered list indicators
            output = output.replace(Regex("^\\s*\\d+\\.\\s"), "")

            // Remove blockquotes
            output = output.replace(Regex("^>\\s"), "")

            return output
        }
    }
}