package com.nafanya.mp3world.view.listActivities.artists

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.listManagers.ArtistListManager
import com.nafanya.mp3world.model.wrappers.Artist
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListActivity
import com.nafanya.mp3world.viewmodel.listViewModels.artists.ArtistListViewModel
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel

class ArtistListActivity : RecyclerHolderActivity() {

    private var artists: List<Artist>? = null

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[ArtistListViewModel::class.java]
        (viewModel as ArtistListViewModel).getData {
            this.artists = it
        }
    }

    override fun setAdapter() {
        binding.recycler.adapter = ArtistListAdapter(ArtistListManager.artists) {
            val intent = Intent(this, SongListActivity::class.java)
            SongListViewModel.newInstanceWithPlaylist(it.playlist)
            startActivity(intent)
        }
    }
}
