package com.nafanya.mp3world.viewmodel.listViewModels.search

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.listManagers.SongListManager
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

    fun addSongToLocalStorage(song: Song) {
        song.date = simpleDateFormat.format(Date())
        SongListManager.addToStorage(song)
    }

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

    fun deleteSongFromLocalStorage(song: Song) {
        SongListManager.deleteFromStorage(song)
    }

    fun isAdded(song: Song): Boolean {
        SongListManager.songList.value?.forEach {
            if (it.url == song.url) return true
        }
        return false
    }
}
