package com.nafanya.mp3world.viewmodel.listViewModels.artists

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.listManagers.ArtistListManager
import com.nafanya.mp3world.model.wrappers.Artist
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState

class ArtistListViewModel : ListViewModelInterface() {

    val artistList: MutableLiveData<MutableList<Artist>> by lazy {
        MutableLiveData<MutableList<Artist>>()
    }

    override fun onLoading() {
        artistList.postValue(ArtistListManager.artists.value)
        pageState.postValue(PageState.IS_LOADED)
    }

    override fun onLoaded() {
        title.postValue("Исполнители (${artistList.value?.size})")
    }
}
