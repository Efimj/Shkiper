package com.jobik.shkiper.helpers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

object LinkUtils {
    fun parseLinks(text: String): Set<String> {
        val regex = Regex("""\b(?:https?://|www\.|http?://)\S+\b""")
        val matches = regex.findAll(text)
        return matches.map { it.value }.toSet()
    }
}

data class LinkPreview(
    val title: String? = null,
    val description: String? = null,
    val img: String? = null,
    val url: String? = null,
    val link: String? = null,
) {
    fun isEmpty(): Boolean {
        return title.isNullOrBlank() && description.isNullOrBlank() && img.isNullOrBlank()
    }
}

suspend fun LinkPreview(
    link: String
): LinkPreview = withContext(Dispatchers.Default) {
    var image: String? = null
    var title: String? = null
    var description: String? = null
    var url: String? = null

    runCatching {
        Jsoup
            .connect(link)
            .execute()
            .parse()
            .getElementsByTag("meta")
            .forEach { element ->
                when (element.attr("property")) {
                    "og:image" -> {
                        image = element.attr("content")
                    }

                    "og:title" -> {
                        title = element.attr("content")
                    }

                    "og:description" -> {
                        description = element.attr("content")
                    }

                    "og:url" -> {
                        url = element.attr("content")
                    }
                }
            }
    }

    LinkPreview(
        link = link,
        img = image,
        title = title,
        description = description,
        url = url
    )
}