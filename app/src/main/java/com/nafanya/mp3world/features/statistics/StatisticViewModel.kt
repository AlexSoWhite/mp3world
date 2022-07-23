package com.nafanya.mp3world.features.statistics

import com.nafanya.mp3world.core.viewModel.ListViewModelInterface
import com.nafanya.mp3world.core.viewModel.PageState

class StatisticViewModel : ListViewModelInterface() {

    override fun onLoading() {
        if (StatisticInfoManager.info.value!!.isEmpty()) {
            pageState.value = PageState.IS_EMPTY
        } else {
            pageState.value = PageState.IS_LOADED
        }
    }

    override fun onLoaded() {
        // TODO
    }

    override fun onEmpty() {
        // TODO
    }
}
