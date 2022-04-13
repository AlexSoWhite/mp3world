package com.nafanya.mp3world.viewmodel.listViewModels.artists

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.listManagers.AlbumListManager
import com.nafanya.mp3world.model.listManagers.ArtistListManager
import com.nafanya.mp3world.model.wrappers.Album
import com.nafanya.mp3world.model.wrappers.Artist
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState

class ArtistListViewModel : ListViewModelInterface() {

    private var query = ""

    val artistList: MutableLiveData<MutableList<Artist>> by lazy {
        MutableLiveData<MutableList<Artist>>()
    }

    override fun onLoading() {
        artistList.value = ArtistListManager.artists.value
        if (artistList.value!!.isEmpty()) {
            pageState.value = PageState.IS_EMPTY
        } else {
            pageState.value = PageState.IS_LOADED
        }
    }

    override fun onLoaded() {
        if (query != "") {
            title.value = "$query (${artistList.value?.size})"
        } else {
            title.value = "Исполнители (${artistList.value?.size})"
        }
    }

    override fun onEmpty() {
        if (query != "") {
            title.value = "Исполнители"
        } else {
            title.value = query
        }
    }


    fun search(query: String) {
        val newList = mutableListOf<Artist>()
        this.query = query
        ArtistListManager.artists.value!!.forEach {
            if (
                it.name!!.lowercase().contains(query.lowercase())
            ) {
                newList.add(it)
            }
        }
        artistList.value = newList
        if (artistList.value!!.isEmpty()) {
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
