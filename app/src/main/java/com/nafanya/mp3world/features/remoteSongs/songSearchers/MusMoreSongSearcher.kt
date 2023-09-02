package com.nafanya.mp3world.features.remoteSongs.songSearchers

import com.google.gson.Gson
import com.nafanya.mp3world.core.utils.timeConverters.TimeConverter
import com.nafanya.mp3world.core.wrappers.UriFactory
import com.nafanya.mp3world.core.wrappers.RemoteSong
import javax.inject.Inject
import okhttp3.OkHttpClient
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class MusMoreSongSearcher @Inject constructor(
    client: OkHttpClient,
    private val uriFactory: UriFactory,
    private val gson: Gson
) : SongSearcher(client) {

    private val searchPrefix = "https://ruo.morsmusic.org/search/"

    private val prefix = "https://ruo.morsmusic.org"

    data class MetaJson(
        val artist: String,
        val title: String,
        val url: String,
        val img: String,
        val id: String,
        val trackUrl: String,
        val dlUrl: String
    )

    override fun getSearchUrl(query: String) = searchPrefix + query

    override fun splitDocument(document: Document): Elements = document.getElementsByClass("track")

    override fun parseNode(index: Int, element: Element): RemoteSong {
        val attrs = element.attributes().dataset()
        val meta = attrs.getValue("musmeta")
        val metaJson = gson.fromJson(meta, MetaJson::class.java)
        val title = metaJson.title
        val artist = metaJson.artist
        val songUrl = prefix + metaJson.url
        val artUrl = metaJson.img.substringBeforeLast("/") + "/300x300"
        val duration = TimeConverter.stringToDuration(
            element.getElementsByClass("track__fulltime").text()
        )
        return RemoteSong(
            uri = uriFactory.getUri(songUrl),
            artUrl = artUrl,
            title = title,
            artist = artist,
            duration = duration
        )
    }
}
