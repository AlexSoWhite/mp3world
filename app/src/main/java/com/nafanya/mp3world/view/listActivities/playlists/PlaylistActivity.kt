package com.nafanya.mp3world.view.listActivities.playlists

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.view.ActivityCreator
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListAdapter
import com.nafanya.mp3world.viewmodel.listViewModels.playlists.AddSongToListViewModel
import com.nafanya.mp3world.viewmodel.listViewModels.playlists.PlaylistViewModel
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel

class PlaylistActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[PlaylistViewModel::class.java]
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
        super.addCustomBehavior()
        binding.addSongToPlaylist.addSongToPlaylist.visibility = View.VISIBLE
        binding.addSongToPlaylist.addSongToPlaylist.setOnClickListener {
            startAddSongActivity()
        }
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptyPlaylist.emptyPlaylist.visibility = View.VISIBLE
        binding.emptyPlaylist.emptyPlaylist.setOnClickListener {
            startAddSongActivity()
        }
    }

    // TODO inject viewModel
    private fun startAddSongActivity() {
        AddSongToListViewModel.newInstance(viewModel as PlaylistViewModel)
        ActivityCreator()
            .createActivityWithPlaylist(
                Playlist(
                    SongListManager.songList.value!!,
                    name = (viewModel as SongListViewModel).playlist.value!!.name
                ),
                AddSongToListActivity::class.java
            )
            .start()
    }
}
