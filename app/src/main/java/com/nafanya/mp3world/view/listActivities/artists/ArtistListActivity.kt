package com.nafanya.mp3world.view.listActivities.artists

import android.content.Intent
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.listManagers.ArtistListManager
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListActivity
import com.nafanya.mp3world.viewmodel.listViewModels.PageState
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel

class ArtistListActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[SongListViewModel::class.java]
        viewModel.pageState.value = PageState.IS_LOADED
    }

    override fun setAdapter() {
        binding.recycler.adapter = ArtistListAdapter(ArtistListManager.artists) {
            val intent = Intent(this, SongListActivity::class.java)
            SongListViewModel.newInstanceWithPlaylist(it.playlist)
            startActivity(intent)
        }
    }

    override fun setTitle() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        supportActionBar?.title = "Исполнители (${ArtistListManager.artists.size})"
    }
}
