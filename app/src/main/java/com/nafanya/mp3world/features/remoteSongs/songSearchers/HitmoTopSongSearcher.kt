package com.nafanya.mp3world.features.remoteSongs.songSearchers

import com.nafanya.mp3world.core.utils.timeConverters.TimeConverter
import com.nafanya.mp3world.core.wrappers.ArtFactory
import com.nafanya.mp3world.core.wrappers.UriFactory
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong
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
// TODO result wrapping
@Deprecated("does not work anymore")
class HitmoTopSongSearcher @Inject constructor(
    client: OkHttpClient,
    private val artFactory: ArtFactory,
    private val uriFactory: UriFactory,
    private val timeConverter: TimeConverter
) : SongSearcher(client) {

    private val prefix = "https://ru.hitmotop.com/search?q="

    private var arts: Elements? = null

    override fun getSearchUrl(query: String) = prefix + query

    override fun splitDocument(document: Document): List<Element> {
        arts = document.getElementsByClass("track__img")
        return document.getElementsByClass("track_info")
    }

    override fun parseNode(index: Int, element: Element): RemoteSong {
        val artElement = arts?.get(index)
        val title = element.getElementsByClass("track__title").text()
        val artist = element.getElementsByClass("track__desc").text()
        val duration = timeConverter.stringToDuration(
            element.getElementsByClass("track__fulltime").text()
        )
        var artUrl = artElement
            ?.attr("style")
        artUrl = artUrl?.substring(
            artUrl.indexOf('\'') + 2,
            artUrl.lastIndexOf('\'')
        )
        artUrl = "https://ru.hitmotop.com/$artUrl"
        val downloadUrl = element
            .getElementsByClass("track__download-btn")
            .attr("href")
            .toString()
        return RemoteSong(
            uri = uriFactory.getUri(downloadUrl),
            art = artFactory.createArtUri(artUrl),
            title = title,
            artist = artist,
            duration = duration
        )
    }
}
