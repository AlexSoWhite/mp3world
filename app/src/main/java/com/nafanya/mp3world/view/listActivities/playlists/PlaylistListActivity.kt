package com.nafanya.mp3world.view.listActivities.playlists

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListActivity
import com.nafanya.mp3world.viewmodel.listViewModels.playlists.PlaylistListViewModel
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel

class PlaylistListActivity : RecyclerHolderActivity() {

    private var playlists: List<Playlist>? = null

    override fun setAdapter() {
        binding.recycler.adapter = PlaylistListAdapter(
            playlists!!
        ) {
            val intent = Intent(this, SongListActivity::class.java)
            SongListViewModel.newInstanceWithPlaylist(it)
            startActivity(intent)
        }
    }

    override fun addCustomBehavior() {
        super.addCustomBehavior()
        binding.addPlaylist.addPlaylist.visibility = View.VISIBLE
        binding.addPlaylist.addPlaylist.setOnClickListener {
            val intent = Intent(this, AddPlaylistDialogActivity::class.java)
            AddPlaylistDialogActivity.setViewModel(viewModel as PlaylistListViewModel)
            startActivity(intent)
        }
    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[PlaylistListViewModel::class.java]
        (viewModel as PlaylistListViewModel).getData {
            playlists = it
        }
    }
}
