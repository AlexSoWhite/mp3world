package com.nafanya.mp3world.features.remoteSongs.songSearchers

import androidx.annotation.WorkerThread
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import java.io.IOException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

abstract class SongSearcher(
    private val client: OkHttpClient
) {

    @WorkerThread
    fun searchSongs(query: String, onSearchCompleted: (List<RemoteSong>?) -> Unit) {
        val request = Request.Builder()
            .url(getSearchUrl(query))
            .build()
        client.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    onSearchCompleted.invoke(null)
                }

                override fun onResponse(call: Call, response: Response) {
                    val list = mutableListOf<RemoteSong>()
                    response.body?.let {
                        val doc = Jsoup.parseBodyFragment(it.string())
                        splitDocument(doc).forEachIndexed { index, element ->
                            val model = parseNode(index, element)
                            list.add(model)
                        }
                    }
                    onSearchCompleted.invoke(list)
                }
            }
        )
    }

    protected abstract fun getSearchUrl(query: String): String

    protected abstract fun splitDocument(document: Document): List<Element>

    protected abstract fun parseNode(index: Int, element: Element): RemoteSong
}
