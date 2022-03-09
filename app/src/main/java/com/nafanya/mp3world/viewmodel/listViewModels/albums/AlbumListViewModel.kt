package com.nafanya.mp3world.viewmodel.listViewModels.albums

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.listManagers.AlbumListManager
import com.nafanya.mp3world.model.wrappers.Album
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState

class AlbumListViewModel : ListViewModelInterface() {

    var albumsList: MutableLiveData<MutableList<Album>> = MutableLiveData<MutableList<Album>>()

    override fun onLoading() {
        albumsList = AlbumListManager.albums
        if (albumsList.value!!.isEmpty()) {
            pageState.value = PageState.IS_EMPTY
        } else {
            pageState.value = PageState.IS_LOADED
        }
    }

    override fun onLoaded() {
        title.value = "Альбомы ${albumsList.value?.size}"
    }

    override fun onEmpty() {
        title.value = "Альбомы"
    }
}
