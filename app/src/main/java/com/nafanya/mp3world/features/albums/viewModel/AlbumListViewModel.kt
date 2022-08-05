package com.nafanya.mp3world.features.albums.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.viewModel.ListViewModelInterface
import com.nafanya.mp3world.core.viewModel.PageState
import com.nafanya.mp3world.features.albums.Album
import com.nafanya.mp3world.features.albums.AlbumListManager
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

class AlbumListViewModel @Inject constructor(
    playerInteractor: PlayerInteractor,
    private val albumListManager: AlbumListManager
) : ListViewModelInterface(playerInteractor) {

    private var query = ""
    private val mAlbumsList: MutableLiveData<List<Album>> = MutableLiveData(listOf())
    val albumsList: LiveData<List<Album>>
        get() = mAlbumsList

    override fun onLoading() {
        mAlbumsList.value = albumListManager.albums.value
        if (mAlbumsList.value!!.isEmpty()) {
            pageState.value = PageState.IS_EMPTY
        } else {
            pageState.value = PageState.IS_LOADED
        }
    }

    override fun onLoaded() {
        if (query != "") {
            title.value = "$query (${mAlbumsList.value?.size})"
        } else {
            title.value = "Альбомы (${mAlbumsList.value?.size})"
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
        this.query = query
        mAlbumsList.value = albumListManager.search(query)
        if (mAlbumsList.value!!.isEmpty()) {
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
