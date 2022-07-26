package com.nafanya.mp3world.core.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nafanya.player.PlayerInteractor
import com.nafanya.player.Playlist
import com.nafanya.player.Song

enum class PageState {
    IS_LOADING,
    IS_LOADED,
    IS_EMPTY
}

abstract class ListViewModelInterface(
    var playerInteractor: PlayerInteractor
) : ViewModel() {

    val isPlayerInitialised
        get() = playerInteractor.isPlayerInitialised

    val pageState: MutableLiveData<PageState> by lazy {
        MutableLiveData<PageState>(PageState.IS_LOADING)
    }

    val title: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    abstract fun onLoading()

    abstract fun onLoaded()

    abstract fun onEmpty()

    fun onClick(playlist: Playlist, song: Song) {
        playerInteractor.setPlaylist(playlist)
        playerInteractor.setSong(song)
    }
//
//    abstract fun onDataAdded(arg: Any)
//
//    abstract fun onDataRemoved(arg: Any)
}
