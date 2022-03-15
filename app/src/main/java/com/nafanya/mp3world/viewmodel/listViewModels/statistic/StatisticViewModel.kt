package com.nafanya.mp3world.viewmodel.listViewModels.statistic

import com.nafanya.mp3world.model.listManagers.StatisticInfoManager
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState

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
