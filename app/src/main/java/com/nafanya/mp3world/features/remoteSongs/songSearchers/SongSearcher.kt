package com.nafanya.mp3world.features.remoteSongs.songSearchers

import com.nafanya.mp3world.core.wrappers.SongList
import com.nafanya.mp3world.core.wrappers.RemoteSong
import java.io.IOException
import kotlinx.coroutines.flow.Flow
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

    protected val mSongList = SongList<RemoteSong>()

    val songList: Flow<List<RemoteSong>?>
        get() = mSongList.listFlow

    fun searchSongs(query: String) {
        val request = Request.Builder()
            .url(getSearchUrl(query))
            .build()
        client.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    mSongList.unlock()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.body?.let {
                        mSongList.lock()
                        mSongList.setEmptyList()
                        val doc = Jsoup.parseBodyFragment(it.string())
                        splitDocument(doc).forEachIndexed { index, element ->
                            val model = parseNode(index, element)
                            mSongList.addOrUpdateSongWrapper(model)
                        }
                    }
                    mSongList.unlock()
                }
            }
        )
    }

    protected abstract fun getSearchUrl(query: String): String

    protected abstract fun splitDocument(document: Document): List<Element>

    protected abstract fun parseNode(index: Int, element: Element): RemoteSong
}
