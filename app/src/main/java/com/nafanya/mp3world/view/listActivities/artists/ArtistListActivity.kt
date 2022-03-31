package com.nafanya.mp3world.view.listActivities.artists

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.wrappers.Artist
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListActivity
import com.nafanya.mp3world.viewmodel.listViewModels.SourceProvider
import com.nafanya.mp3world.viewmodel.listViewModels.artists.ArtistListViewModel

class ArtistListActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[ArtistListViewModel::class.java]
    }

    override fun setAdapter() {
        val observer = Observer<MutableList<Artist>> {
            binding.recycler.adapter = ArtistListAdapter(it) { artist ->
                val intent = Intent(this, SongListActivity::class.java)
                SourceProvider.newInstanceWithPlaylist(artist.playlist!!)
                startActivity(intent)
            }
        }
        (viewModel as ArtistListViewModel).artistList.observe(this, observer)
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptyArtistList.emptyArtistList.visibility = View.VISIBLE
    }
}
