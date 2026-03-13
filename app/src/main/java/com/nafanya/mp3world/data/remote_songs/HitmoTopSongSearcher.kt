package com.nafanya.mp3world.data.remote_songs

import com.nafanya.mp3world.core.utils.time_converters.TimeConverter
import com.nafanya.mp3world.core.wrappers.song.ArtistMetadata
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.core.wrappers.song.UriFactory
import com.nafanya.mp3world.core.wrappers.song.splitArtistNames
import javax.inject.Inject
import okhttp3.OkHttpClient
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * Class that makes calls to api.
 * Will be replaced with SoundCloud API call when it opens.
 *
 * CAUTION: this should not be a singleton
 */
// TODO pagination
class HitmoTopSongSearcher @Inject constructor(
    client: OkHttpClient,
    private val uriFactory: UriFactory
) : SongSearcher(client) {

    private val baseUrl = "https://rus.hitmotop.com"

    private val searchPrefix = "$baseUrl/search?q="

    override fun getSearchUrl(query: String) = searchPrefix + query

    override fun splitDocument(document: Document): Elements =
        document.getElementsByClass("tracks__item")

    private var localArtistId = 0L
    private val localArtistIdMap = mutableMapOf<String, Long>()
    private val localArtistNameCaseMap = mutableMapOf<String, String>()

    override fun parseNode(index: Int, element: Element): RemoteSong {
        val title = element.getElementsByClass("track__title").text()
        val artists = mutableListOf<ArtistMetadata>()
        element.getElementsByClass("track__desc").text().splitArtistNames().forEach { name ->
            val key = name.lowercase()
            if (!localArtistIdMap.contains(key)) {
                localArtistIdMap[key] = localArtistId++
                localArtistNameCaseMap[key] = name
            }
            artists.add(
                ArtistMetadata(
                    id = localArtistIdMap[key]!!,
                    name = localArtistNameCaseMap[key]!!
                )
            )
        }
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
            artists = artists,
            duration = duration
        )
    }
}
