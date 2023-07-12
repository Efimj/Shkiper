package com.android.notepad.helpers

import android.util.Log
import org.jsoup.Jsoup

class LinkHelper {
    fun findLinks(text: String): Set<String> {
        val regex = Regex("""\b(?:https?://|www\.)\S+\b""")
        val matches = regex.findAll(text)
        return matches.map { it.value }.toSet()
    }

    data class LinkPreview(
        var title: String? = null,
        var description: String? = null,
        var img: String? = null,
        var url: String? = null,
        var link: String? = null,
    )

    suspend fun getOpenGraphData(link: String): LinkPreview {
        val linkPreview = LinkPreview(link = link)
        try {
            val response = Jsoup.connect(link).execute()
            val docs = response.parse().getElementsByTag("meta")
            for (element in docs) {
                when {
                    element.attr("property").equals("og:image") -> {
                        linkPreview.img = element.attr("content")
                    }
                    element.attr("property").equals("og:title") -> {
                        linkPreview.title = element.attr("content")
                    }
                    element.attr("property").equals("og:description") -> {
                        linkPreview.description = element.attr("content")
                    }
                    element.attr("property").equals("og:url") -> {
                        linkPreview.url = element.attr("content")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("getOpenGraphData", "Error occurred", e)
        }
        return linkPreview
    }
}