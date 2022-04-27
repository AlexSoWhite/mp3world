package com.nafanya.mp3world.view.listActivities.playlists

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.view.ActivityCreator
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListAdapter
import com.nafanya.mp3world.viewmodel.listViewModels.playlists.PlaylistViewModel
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = if (intent.hasExtra("isMutable") && intent.getBooleanExtra("isMutable", false)) {
            ViewModelProvider(this)[PlaylistViewModel::class.java]
        } else {
            ViewModelProvider(this)[SongListViewModel::class.java]
        }
    }

    override fun setAdapter() {
        val observer = Observer<Playlist> {
            binding.recycler.adapter = SongListAdapter(it.songList, this) {
                PlayerLiveDataProvider.currentPlaylist.value = it
            }
        }
        (viewModel as SongListViewModel).playlist.observe(this, observer)
    }

    override fun addCustomBehavior() {
        if (intent.hasExtra("isMutable") && intent.getBooleanExtra("isMutable", false)) {
            super.addCustomBehavior()
            binding.addSongToPlaylist.addSongToPlaylist.visibility = View.VISIBLE
            binding.addSongToPlaylist.addSongToPlaylist.setOnClickListener {
                startAddSongActivity()
            }
        }
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptyPlaylist.emptyPlaylist.visibility = View.VISIBLE
        if (intent.hasExtra("isMutable") && intent.getBooleanExtra("isMutable", false)) {
            binding.emptyPlaylist.emptyPlaylist.setOnClickListener {
                startAddSongActivity()
            }
        }
    }

    // TODO inject viewModel
    private fun startAddSongActivity() {
        ActivityCreator()
            .with(this)
            .createAddSongToListActivity(viewModel as PlaylistViewModel)
            .start()
    }
}
