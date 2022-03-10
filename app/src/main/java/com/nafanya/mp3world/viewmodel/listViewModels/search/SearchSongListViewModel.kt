package com.nafanya.mp3world.viewmodel.listViewModels.search

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.localStorage.LocalStorageProvider
import com.nafanya.mp3world.model.network.Downloader
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState
import com.nafanya.mp3world.viewmodel.listViewModels.SourceProvider
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SearchSongListViewModel : ListViewModelInterface() {

    val playlist: MutableLiveData<Playlist> by lazy {
        MutableLiveData<Playlist>()
    }

    private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))

    private fun startLoading(query: String, callback: (Playlist) -> Unit) {
        Downloader.preLoad(query) { playlist ->
            playlist?.let {
                callback(playlist)
            }
        }
    }

    override fun onLoading() {
        val initializingQuery = SourceProvider.getQuery()
        val initializingPlaylist = SourceProvider.getPlaylist()
        when {
            initializingQuery != null -> {
                title.postValue(initializingQuery)
                startLoading(initializingQuery) {
                    playlist.postValue(it)
                    if (it.songList.isEmpty()) {
                        pageState.postValue(PageState.IS_EMPTY)
                    } else {
                        pageState.postValue(PageState.IS_LOADED)
                    }
                }
            }
            initializingPlaylist != null -> {
                title.value = initializingPlaylist.name
                playlist.value = initializingPlaylist
                if (playlist.value?.songList!!.isEmpty()) {
                    pageState.value = PageState.IS_EMPTY
                } else {
                    pageState.value = PageState.IS_LOADED
                }
            }
            else -> {
                throw(RuntimeException("song list must be initialized with query or playlist"))
            }
        }
    }

    override fun onLoaded() {
        // TODO
    }

    override fun onEmpty() {
        // TODO
    }

    fun addSong(context: Context, song: Song) {
        song.date = simpleDateFormat.format(Date())
        // modifying LiveData
        SongListManager.addSongWithUrl(song)
        // adding song to the   local storage
        LocalStorageProvider.addSong(context, song)
    }

    fun deleteSong(context: Context, song: Song) {
        // modifying LiveData
        SongListManager.deleteSongWithUrl(song)
        // removing song from the local storage
        LocalStorageProvider.deleteSong(context, song)
    }

    fun isAdded(song: Song): Boolean {
        SongListManager.songList.value?.forEach {
            if (it.url == song.url) {
                song.id = it.id
                return true
            }
        }
        return false
    }
}
