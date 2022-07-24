package com.nafanya.mp3world.features.playlists.playlist.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.view.ActivityCreator
import com.nafanya.mp3world.core.view.RecyclerHolderActivity
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.features.allSongs.SongListAdapter
import com.nafanya.mp3world.features.allSongs.SongListViewModel
import com.nafanya.mp3world.features.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.features.playlists.playlist.Playlist
import com.nafanya.mp3world.features.playlists.playlist.viewModel.PlaylistViewModel
import javax.inject.Inject

// TODO consider separation
class PlaylistActivity : RecyclerHolderActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    override fun setViewModel() {
        viewModel = if (
            intent.hasExtra("isMutable") &&
            intent.getBooleanExtra("isMutable", false)
        ) {
            factory.create(PlaylistViewModel::class.java)
        } else {
            factory.create(SongListViewModel::class.java)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PlayerApplication).applicationComponent.playlistComponent().inject(this)
        super.onCreate(savedInstanceState)
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
    // TODO navigation
    private fun startAddSongActivity() {
        ActivityCreator()
            .with(this)
            .createAddSongToListActivity(viewModel as PlaylistViewModel)
            .start()
    }
}
