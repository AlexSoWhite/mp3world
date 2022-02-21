package com.nafanya.mp3world.view.listActivities.playlists

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.viewmodel.listViewModels.SourceProvider
import com.nafanya.mp3world.viewmodel.listViewModels.playlists.PlaylistListViewModel
import com.nafanya.mp3world.viewmodel.listViewModels.playlists.PlaylistViewModel

class PlaylistListActivity : RecyclerHolderActivity() {

    override fun setAdapter() {
        val observer = Observer<MutableList<Playlist>> {
            binding.recycler.adapter = PlaylistListAdapter(
                it
            ) { playlist ->
                val intent = Intent(this, PlaylistActivity::class.java)
                SourceProvider.newInstanceWithPlaylist(playlist)
                PlaylistViewModel.newInstance(viewModel as PlaylistListViewModel)
                startActivity(intent)
            }
        }
        (viewModel as PlaylistListViewModel).playlists.observe(this, observer)
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
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptyPlaylistList.emptyPlaylistList.visibility = View.VISIBLE
        binding.emptyPlaylistList.emptyPlaylistList.setOnClickListener {
            val intent = Intent(this, AddPlaylistDialogActivity::class.java)
            AddPlaylistDialogActivity.setViewModel(viewModel as PlaylistListViewModel)
            startActivity(intent)
        }
    }
}
