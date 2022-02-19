package com.nafanya.mp3world.view.listActivities.playlists

import android.content.Intent
import android.view.View
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_TITLE
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListActivity
import com.nafanya.mp3world.viewmodel.listViewModels.PageState
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel

class PlaylistListActivity : RecyclerHolderActivity() {

    override fun setAdapter() {
        binding.recycler.adapter = PlaylistListAdapter(
            PlaylistListManager.playlists
        ) {
            val intent = Intent(this, SongListActivity::class.java)
            SongListViewModel.newInstanceWithPlaylist(it)
            startActivity(intent)
        }
    }

    override fun setTitle() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.displayOptions = DISPLAY_SHOW_TITLE
        supportActionBar?.title = "Мои плейлисты (${PlaylistListManager.playlists.size})"
    }

    override fun addCustomBehavior() {
        super.addCustomBehavior()
        binding.addPlaylist.addPlaylist.visibility = View.VISIBLE
        binding.addPlaylist.addPlaylist.setOnClickListener {
            val intent = Intent(this, AddPlaylistDialogActivity::class.java)
            startActivity(intent)
        }
    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[SongListViewModel::class.java]
        viewModel.pageState.value = PageState.IS_LOADED
    }
}
