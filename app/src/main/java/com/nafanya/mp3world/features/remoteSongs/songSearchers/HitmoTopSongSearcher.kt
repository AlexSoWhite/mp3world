package com.nafanya.mp3world.features.remoteSongs.songSearchers

import com.nafanya.mp3world.core.utils.timeConverters.TimeConverter
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.core.wrappers.song.UriFactory
import javax.inject.Inject
import okhttp3.OkHttpClient
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * Class that makes calls to api.
 * Will be replaced with SoundCloud API call when it opens.
 */
// TODO pagination
class HitmoTopSongSearcher @Inject constructor(
    client: OkHttpClient,
    private val uriFactory: UriFactory
) : SongSearcher(client) {

    private val baseUrl = "https://rur.hitmotop.com"

    private val searchPrefix = "$baseUrl/search?q="

    override fun getSearchUrl(query: String) = searchPrefix + query

    override fun splitDocument(document: Document): Elements =
        document.getElementsByClass("tracks__item")

    override fun parseNode(index: Int, element: Element): RemoteSong {
        val title = element.getElementsByClass("track__title").text()
        val artist = element.getElementsByClass("track__desc").text()
        val duration = TimeConverter.stringToDuration(
            element.getElementsByClass("track__fulltime").text()
        )
        val artUrl = element
            .getElementsByClass("track__img")
            .attr("style")
            .substringAfter('\'')
            .substringBefore('\'')
        val downloadUrl = element
            .getElementsByClass("track__download-btn")
            .attr("href")
            .toString()
        return RemoteSong(
            uri = uriFactory.getUri(downloadUrl),
            artUrl = artUrl,
            title = title,
            artist = artist,
            duration = duration
        )
    }
}
