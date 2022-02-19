package com.nafanya.mp3world.view.listActivities.songs

import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel

class SongListActivity : RecyclerHolderActivity() {

    private var playlist: Playlist? = null

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[SongListViewModel::class.java]
        (viewModel as SongListViewModel).getData() {
            this.playlist = it
        }
    }

    override fun setAdapter() {
        binding.recycler.adapter = SongListAdapter(playlist!!.songList) {
            ForegroundServiceLiveDataProvider.currentPlaylist.value = playlist
        }
    }

    override fun setTitle() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        val observer = Observer<String> {
            supportActionBar?.title = it
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        (viewModel as SongListViewModel).title.observe(this, observer)
    }
}
