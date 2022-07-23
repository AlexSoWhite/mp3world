package com.nafanya.mp3world.features.statistics

import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.core.view.RecyclerHolderActivity

class StatisticActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[StatisticViewModel::class.java]
    }

    override fun setAdapter() {
        binding.recycler.adapter = StatisticListAdapter(StatisticInfoManager.info.value!!)
    }

    override fun setTitle() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        supportActionBar?.title = "Статистика"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptySongList.emptySongList.visibility = View.VISIBLE
    }
}
