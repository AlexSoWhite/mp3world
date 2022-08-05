package com.nafanya.mp3world.features.artists.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.viewModel.ListViewModelInterface
import com.nafanya.mp3world.core.viewModel.PageState
import com.nafanya.mp3world.features.artists.Artist
import com.nafanya.mp3world.features.artists.ArtistListManager
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

class ArtistListViewModel @Inject constructor(
    playerInteractor: PlayerInteractor,
    private val artistListManager: ArtistListManager
) : ListViewModelInterface(playerInteractor) {

    private var query = ""

    private val mArtistList: MutableLiveData<List<Artist>> = MutableLiveData(listOf())
    val artistList: LiveData<List<Artist>>
        get() = mArtistList

    override fun onLoading() {
        mArtistList.value = artistListManager.artists.value
        if (mArtistList.value!!.isEmpty()) {
            pageState.value = PageState.IS_EMPTY
        } else {
            pageState.value = PageState.IS_LOADED
        }
    }

    override fun onLoaded() {
        if (query != "") {
            title.value = "$query (${mArtistList.value?.size})"
        } else {
            title.value = "Исполнители (${mArtistList.value?.size})"
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
        this.query = query
        mArtistList.value = artistListManager.search(query)
        if (mArtistList.value!!.isEmpty()) {
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
