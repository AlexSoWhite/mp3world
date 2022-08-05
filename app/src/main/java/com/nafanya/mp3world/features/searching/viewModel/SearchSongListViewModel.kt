package com.nafanya.mp3world.features.searching.viewModel

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.viewModel.ListViewModelInterface
import com.nafanya.mp3world.core.viewModel.PageState
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.remoteSongs.QueryExecutor
import com.nafanya.player.PlayerInteractor
import com.nafanya.player.Playlist
import com.nafanya.player.Song
import javax.inject.Inject

/**
 TODO: error
 */
class SearchSongListViewModel @Inject constructor(
    private val initializingQuery: String,
    private val queryExecutor: QueryExecutor,
    private val songListManager: SongListManager,
    playerInteractor: PlayerInteractor
) : ListViewModelInterface(playerInteractor) {

    val playlist: MutableLiveData<Playlist> by lazy {
        MutableLiveData<Playlist>()
    }

    private fun startLoading(query: String, callback: (List<Song>?) -> Unit) {
        queryExecutor.preLoad(query) { songList ->
            callback(songList)
        }
    }

    override fun onLoading() {
        title.postValue(initializingQuery)
        val songList = mutableListOf<Song>()
        songList.addAll(songListManager.searchForSongs(initializingQuery))
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

    override fun onLoaded() {
        // TODO
    }

    override fun onEmpty() {
        // TODO
    }
}
