package com.nafanya.mp3world.viewmodel.listViewModels.songs

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.network.Downloader
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState
import java.lang.RuntimeException

class SongListViewModel : ListViewModelInterface() {

    val playlist: MutableLiveData<Playlist> by lazy {
        MutableLiveData<Playlist>()
    }

    private fun startLoading(query: String, callback: (Playlist) -> Unit) {
        Downloader.preLoad(query) { playlist ->
            playlist?.let {
                callback(playlist)
            }
        }
    }

    override fun onLoading() {
        when {
            query != null -> {
                title.postValue(query)
                startLoading(query!!) {
                    playlist.postValue(it)
                    if (it.songList.isEmpty()) {
                        pageState.postValue(PageState.IS_EMPTY)
                    } else {
                        pageState.postValue(PageState.IS_LOADED)
                    }
                }
            }
            initializingPlaylist != null -> {
                title.value = initializingPlaylist!!.name
                playlist.value = initializingPlaylist!!
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
        title.value =
            "${playlist.value!!.name} ${'(' + playlist.value!!.songList.size.toString() + ')'}"
    }

    override fun onEmpty() {

    }

    companion object {

        private var initializingPlaylist: Playlist? = null
        private var query: String? = null

        fun newInstanceWithPlaylist(
            playlist: Playlist
        ) {
            this.initializingPlaylist = playlist
            this.query = null
        }
        fun newInstanceWithQuery(
            query: String
        ) {
            this.query = query
            this.initializingPlaylist = null
        }
    }
}
