package com.nafanya.mp3world.features.remoteSongs

import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.utils.timeConverters.TimeConverter
import com.nafanya.mp3world.core.wrappers.ArtFactory
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongList
import com.nafanya.mp3world.core.wrappers.UriFactory
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup

/**
 * Class that makes calls to api.
 * Will be replaced with SoundCloud API call when it opens.
 */
// TODO pagination
// TODO result wrapping
class QueryExecutor @Inject constructor(
    private val client: OkHttpClient,
    private val artFactory: ArtFactory,
    private val timeConverter: TimeConverter,
    private val ioCoroutineProvider: IOCoroutineProvider
) {

    private val prefix = "https://ru.hitmotop.com/search?q="

    private val mSongList = SongList<RemoteSong>()
    val songList: Flow<List<RemoteSong>?>
        get() = mSongList.listFlow

    fun executeQuery(query: String) {
        val url = prefix + query
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    mSongList.unlock()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.body?.let {
                        mSongList.setEmptyList()
                        val doc = Jsoup.parseBodyFragment(it.string())
                        val arts = doc.getElementsByClass("track__img")
                        val info = doc.getElementsByClass("track__info")
                        for (i in info.indices) {
                            val elem = info[i]
                            val artElement = arts[i]
                            val title = elem.getElementsByClass("track__title").text()
                            val artist = elem.getElementsByClass("track__desc").text()
                            val duration = timeConverter.stringToDuration(
                                elem.getElementsByClass("track__fulltime").text()
                            )
                            var artUrl = artElement
                                .attr("style")
                            artUrl = artUrl.substring(
                                artUrl.indexOf('\'') + 2,
                                artUrl.lastIndexOf('\'')
                            )
                            artUrl = "https://ru.hitmotop.com/$artUrl"
                            val downloadUrl = elem
                                .getElementsByClass("track__download-btn")
                                .attr("href")
                                .toString()
                            val uri = UriFactory().getUri(downloadUrl)
                            mSongList.addOrUpdateSongWrapper(
                                RemoteSong(
                                    uri = uri,
                                    title = title,
                                    artist = artist,
                                    duration = duration,
                                    art = artFactory.defaultBitmap
                                )
                            )
                            ioCoroutineProvider.ioScope.launch {
                                artFactory.createBitmap(artUrl).collect { bitmap ->
                                    val newSong = RemoteSong(
                                        uri = uri,
                                        title = title,
                                        artist = artist,
                                        duration = duration,
                                        art = bitmap
                                    )
                                    mSongList.addOrUpdateSongWrapper(newSong)
                                }
                            }
                        }
                    }
                    mSongList.unlock()
                }
            }
        )
    }
}

fun List<RemoteSong>?.asPlaylist(query: String): PlaylistWrapper {
    return PlaylistWrapper(
        songList = this ?: emptyList(),
        name = query,
        id = -1
    )
}
