package com.nafanya.mp3world.view.listActivities.songs

import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel

class SongListActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[SongListViewModel::class.java]
    }

    override fun setAdapter() {
        val observer = Observer<Playlist> {
            binding.recycler.adapter = SongListAdapter(it.songList, this) {
                PlayerLiveDataProvider.currentPlaylist.value = it
            }
        }
        (viewModel as SongListViewModel).playlist.observe(this, observer)
    }

    override fun setTitle() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        val observer = Observer<String> {
            supportActionBar?.title = it
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        (viewModel as SongListViewModel).title.observe(this, observer)
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptySongList.emptySongList.visibility = View.VISIBLE
    }
}
