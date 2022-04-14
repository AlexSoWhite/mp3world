package com.nafanya.mp3world.viewmodel.listViewModels.search

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.network.QueryExecutor
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState
import com.nafanya.mp3world.viewmodel.listViewModels.SourceProvider
import java.lang.RuntimeException

/**
 TODO: error
 */
class SearchSongListViewModel : ListViewModelInterface() {

    val playlist: MutableLiveData<Playlist> by lazy {
        MutableLiveData<Playlist>()
    }

    private fun startLoading(query: String, callback: (List<Song>?) -> Unit) {
        QueryExecutor().preLoad(query) { songList ->
            callback(songList)
        }
    }

    override fun onLoading() {
        val initializingQuery = SourceProvider.getQuery()
        val initializingPlaylist = SourceProvider.getPlaylist()
        when {
            initializingQuery != null -> {
                title.postValue(initializingQuery)
                val songList = mutableListOf<Song>()
                songList.addAll(SongListManager.searchForSongs(initializingQuery))
                startLoading(initializingQuery) {
                    if ((it == null || it.isEmpty()) && songList.isEmpty()) {
                        pageState.postValue(PageState.IS_EMPTY)
                    } else if (it != null && it.isNotEmpty() || songList.isNotEmpty()) {
                        it?.let { it1 -> songList.addAll(it1) }
                        playlist.postValue(
                            Playlist(
                                name = initializingQuery,
                                songList = songList,
                                id = -1
                            )
                        )
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
}
