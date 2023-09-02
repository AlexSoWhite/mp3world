package com.nafanya.mp3world.features.remoteSongs.songSearchers

import com.nafanya.mp3world.core.wrappers.UriFactory
import com.nafanya.mp3world.core.wrappers.RemoteSong
import javax.inject.Inject
import okhttp3.OkHttpClient
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

@Deprecated("does not contain valid metadata")
class ZvuchSongSearcher @Inject constructor(
    client: OkHttpClient,
    private val uriFactory: UriFactory
) : SongSearcher(client) {

    companion object {
        private const val millisecondsInOneSecond = 1000L
    }

    private val prefix = "https://wwv.zvuch.com"

    override fun getSearchUrl(query: String) = "$prefix/$query"

    override fun splitDocument(document: Document): List<Element> =
        document
            .getElementsByClass("mainSongs")[0]
            .getElementsByClass("item")

    override fun parseNode(index: Int, element: Element): RemoteSong {
        val title = element
            .attributes()
            .get("data-title")
            .substringBefore(" (")
        val artist = element.attributes().get("data-artist")
        val songUrl = element
            .getElementsByClass("play")[0]
            .attributes()
            .get("data-url")
        val artUrl = prefix + element
            .getElementsByClass("playlistImg")[0]
            .getElementsByTag("img")[0]
            .attributes()
            .get("data-src")
            .replace("small", "large")
        val duration = element.attributes().get("data-duration").toLong() * millisecondsInOneSecond
        return RemoteSong(
            uri = uriFactory.getUri(songUrl),
            artUrl = artUrl,
            title = title,
            artist = artist,
            duration = duration
        )
    }
}
