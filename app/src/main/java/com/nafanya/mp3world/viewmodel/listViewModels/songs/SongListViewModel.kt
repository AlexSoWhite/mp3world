package com.nafanya.mp3world.viewmodel.listViewModels.songs

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * TODO refactor initializing and reloading
 */
@HiltViewModel
open class SongListViewModel @Inject constructor(
    val initializingPlaylist: Playlist
) : ListViewModelInterface() {

    val playlist: MutableLiveData<Playlist> by lazy {
        MutableLiveData<Playlist>()
    }
    private var isInitialized = false
    private var query = ""

    override fun onLoading() {
        if (query == "") {
            title.value = initializingPlaylist.name
            playlist.value = initializingPlaylist
            if (playlist.value?.songList!!.isEmpty()) {
                pageState.value = PageState.IS_EMPTY
            } else {
                pageState.value = PageState.IS_LOADED
            }
        } else if (playlist.value!!.songList.isNotEmpty()) {
            pageState.value = PageState.IS_LOADED
        } else {
            pageState.value = PageState.IS_EMPTY
        }
    }

    override fun onLoaded() {
        if (query != "") {
            title.value =
                "$query (${playlist.value!!.songList.size})"
        } else {
            title.value =
                "${playlist.value!!.name} (${playlist.value!!.songList.size})"
        }
    }

    override fun onEmpty() {
        if (query != "") {
            title.value = query
        } else {
            title.value = playlist.value!!.name
        }
    }

    fun start() {
        if (!isInitialized) {
            isInitialized = true
        } else {
            pageState.postValue(PageState.IS_LOADING)
        }
    }

    fun updateData(arg: Playlist) {
        playlist.value = arg
        if (playlist.value!!.songList.isEmpty()) {
            pageState.value = PageState.IS_EMPTY
        } else {
            pageState.value = PageState.IS_LOADED
        }
    }

    fun search(query: String) {
        val newList = mutableListOf<Song>()
        newList.addAll(SongListManager.searchForSongs(query))
        this.query = query
        playlist.value = Playlist(
            songList = newList,
            name = query,
            id = -1
        )
        if (playlist.value!!.songList.isEmpty()) {
            pageState.value = PageState.IS_EMPTY
        } else {
            pageState.value = PageState.IS_LOADED
        }
    }

    fun reset() {
        query = ""
        pageState.value = PageState.IS_LOADING
    }
}
