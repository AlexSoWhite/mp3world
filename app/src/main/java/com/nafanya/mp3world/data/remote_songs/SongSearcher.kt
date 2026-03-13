package com.nafanya.mp3world.data.remote_songs

import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import java.io.IOException
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine
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

    suspend fun searchSongs(query: String): List<RemoteSong>? {
        return suspendCancellableCoroutine { continuation ->
            val request = Request.Builder()
                .url(getSearchUrl(query))
                .build()
            client.newCall(request).enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        continuation.resume(null)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val list = mutableListOf<RemoteSong>()
                        response.body.run {
                            val doc = Jsoup.parseBodyFragment(string())
                            splitDocument(doc).forEachIndexed { index, element ->
                                val model = parseNode(index, element)
                                list.add(model)
                            }
                        }
                        continuation.resume(list)
                    }
                }
            )
        }
    }

    protected abstract fun getSearchUrl(query: String): String

    protected abstract fun splitDocument(document: Document): List<Element>

    protected abstract fun parseNode(index: Int, element: Element): RemoteSong
}
