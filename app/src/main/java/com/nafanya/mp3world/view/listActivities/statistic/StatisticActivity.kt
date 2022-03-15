package com.nafanya.mp3world.view.listActivities.statistic

import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.listManagers.StatisticInfoManager
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel
import com.nafanya.mp3world.viewmodel.listViewModels.statistic.StatisticViewModel

class StatisticActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[StatisticViewModel::class.java]
    }

    override fun setAdapter() {
        binding.recycler.adapter = StatisticListAdapter(StatisticInfoManager.info.value!!)
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptySongList.emptySongList.visibility = View.VISIBLE
    }
}
