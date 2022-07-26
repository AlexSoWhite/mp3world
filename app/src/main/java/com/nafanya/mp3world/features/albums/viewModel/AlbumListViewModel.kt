package com.nafanya.mp3world.features.albums.viewModel

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.viewModel.ListViewModelInterface
import com.nafanya.mp3world.core.viewModel.PageState
import com.nafanya.mp3world.features.albums.Album
import com.nafanya.mp3world.features.albums.AlbumListManager
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

class AlbumListViewModel @Inject constructor(
    playerInteractor: PlayerInteractor
) : ListViewModelInterface(playerInteractor) {

    private var query = ""

    val albumsList: MutableLiveData<MutableList<Album>> by lazy {
        MutableLiveData<MutableList<Album>>()
    }

    override fun onLoading() {
        albumsList.value = AlbumListManager.albums.value
        if (albumsList.value!!.isEmpty()) {
            pageState.value = PageState.IS_EMPTY
        } else {
            pageState.value = PageState.IS_LOADED
        }
    }

    override fun onLoaded() {
        if (query != "") {
            title.value = "$query (${albumsList.value?.size})"
        } else {
            title.value = "Альбомы (${albumsList.value?.size})"
        }
    }

    override fun onEmpty() {
        if (query != "") {
            title.value = "Альбомы"
        } else {
            title.value = query
        }
    }

    fun search(query: String) {
        val newList = mutableListOf<Album>()
        this.query = query
        AlbumListManager.albums.value!!.forEach {
            if (
                it.name.lowercase().contains(query.lowercase())
            ) {
                newList.add(it)
            }
        }
        albumsList.value = newList
        if (albumsList.value!!.isEmpty()) {
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
