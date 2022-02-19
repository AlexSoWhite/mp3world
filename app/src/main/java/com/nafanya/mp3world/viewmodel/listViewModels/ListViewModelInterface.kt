package com.nafanya.mp3world.viewmodel.listViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class PageState {
    IS_LOADING,
    IS_LOADED
}

abstract class ListViewModelInterface : ViewModel() {

    val pageState: MutableLiveData<PageState> by lazy {
        MutableLiveData<PageState>()
    }
}
