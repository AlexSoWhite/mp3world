package com.nafanya.mp3world.model.network

import com.bumptech.glide.Glide
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
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

    fun preLoad(query: String, callback: (Playlist?) -> Unit?) {
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
                        val songList: ArrayList<Song> = arrayListOf()
                        val arts = doc.getElementsByClass("track__img")
                        val info = doc.getElementsByClass("track__info")
                        for (i in info.indices) {
                            val elem = info[i]
                            val art = arts[i]
                            val title = elem.getElementsByClass("track__title").text()
                            val artist = elem.getElementsByClass("track__desc").text()
                            val duration = textToDuration(elem.getElementsByClass("track__fulltime").text())
                            var artUrl = art
                                .attr("style")
                            artUrl = artUrl.substring(
                                artUrl.indexOf('\'')+2,
                                artUrl.lastIndexOf('\'')
                            )
                            artUrl = "https://ru.hitmotop.com/$artUrl"
                            val downloadUrl = elem
                                .getElementsByClass("track__download-btn")
                                .attr("href")
                                .toString()
                            songList.add(
                                Song(
                                    id = SongListManager.urlBasedCount++,
                                    title = title,
                                    artist = artist,
                                    date = "",
                                    duration = duration,
                                    url = downloadUrl,
                                    artUrl = artUrl
                                )
                            )
                        }
                        if (songList.isEmpty()) {
                            callback(null)
                        } else {
                            callback(Playlist(songList, id = -1, name = query))
                        }
                    }
                }
            }
        )
    }

    private const val millisecondsInOneHour = 3600000
    private const val millisecondsInOneMinute = 60000
    private const val millisecondsInOneSecond = 1000

    private fun textToDuration(value: String): Long {
        var result = 0L
        val split = value.split(':').reversed() as ArrayList<String>
        result += (split[0].toInt()) * millisecondsInOneSecond
        if (split.size > 1) result += (split[1].toInt()) * millisecondsInOneMinute
        if (split.size > 2) result += (split[2].toInt()) * millisecondsInOneHour
        return result
    }
}
