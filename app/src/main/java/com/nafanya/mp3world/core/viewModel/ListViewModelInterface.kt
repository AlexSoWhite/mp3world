package com.nafanya.mp3world.core.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class PageState {
    IS_LOADING,
    IS_LOADED,
    IS_EMPTY
}

abstract class ListViewModelInterface : ViewModel() {

    val pageState: MutableLiveData<PageState> by lazy {
        MutableLiveData<PageState>(PageState.IS_LOADING)
    }

    val title: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    abstract fun onLoading()

    abstract fun onLoaded()

    abstract fun onEmpty()
//
//    abstract fun onDataAdded(arg: Any)
//
//    abstract fun onDataRemoved(arg: Any)
}
