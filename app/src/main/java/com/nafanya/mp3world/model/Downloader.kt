package com.nafanya.mp3world.model

import android.util.Log
import com.nafanya.mp3world.viewmodel.ForegroundServiceLiveDataProvider
import java.io.IOException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup

object Downloader {

    private val client = OkHttpClient()
    private const val prefix = "https://ru.hitmotop.com/search?q="

    fun download(name: String, callback: (Playlist?) -> Unit?) {
        val url = prefix + name
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
                        val songList: ArrayList<Song> = arrayListOf()
                        doc.getElementsByClass("track__info").forEach { elem ->
                            val title = elem.getElementsByClass("track__title").text()
                            val artist = elem.getElementsByClass("track__desc").text()
                            val downloadUrl = elem
                                .getElementsByClass("track__download-btn")
                                .attr("href")
                                .toString()
                            songList.add(
                                Song(
                                    id = 0,
                                    title = title,
                                    artist = artist,
                                    date = "",
                                    url = downloadUrl
                                )
                            )
                        }
                        if (songList.isEmpty()) {
                            callback(null)
                        } else {
                            callback(Playlist(songList))
                        }
                    }
                }
            }
        )
    }
}
