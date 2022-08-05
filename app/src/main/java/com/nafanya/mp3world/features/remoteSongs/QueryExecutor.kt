package com.nafanya.mp3world.features.remoteSongs

import com.nafanya.mp3world.core.utils.timeConverters.TimeConverter
import com.nafanya.player.Song
import java.io.IOException
import javax.inject.Inject
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
    private val client: OkHttpClient
) {

    private val prefix = "https://ru.hitmotop.com/search?q="

    fun preLoad(query: String, callback: (List<Song>?) -> Unit?) {
        val url = prefix + query
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    callback(null)
                }

                override fun onResponse(call: Call, response: Response) {
                    response.body?.let Playlist@{
                        val doc = Jsoup.parseBodyFragment(it.string())
                        val songList = mutableListOf<com.nafanya.player.Song>()
                        val arts = doc.getElementsByClass("track__img")
                        val info = doc.getElementsByClass("track__info")
                        for (i in info.indices) {
                            val elem = info[i]
                            val art = arts[i]
                            val title = elem.getElementsByClass("track__title").text()
                            val artist = elem.getElementsByClass("track__desc").text()
                            val duration = TimeConverter().stringToDuration(
                                elem.getElementsByClass("track__fulltime").text()
                            )
                            var artUrl = art
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
                            songList.add(
                                Song(
                                    id = -1,
                                    title = title,
                                    artistId = -1,
                                    artist = artist,
                                    duration = duration,
                                    url = downloadUrl,
                                    artUrl = artUrl,
                                    albumId = -1,
                                    album = "",
                                    date = null,
                                    art = null
                                )
                            )
                        }
                        if (songList.isEmpty()) {
                            callback(null)
                        } else {
                            callback(songList)
                        }
                    }
                }
            }
        )
    }
}
