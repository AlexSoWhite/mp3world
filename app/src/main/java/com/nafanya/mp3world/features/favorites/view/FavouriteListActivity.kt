package com.nafanya.mp3world.features.favorites.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.view.RecyclerHolderActivity
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.features.allSongs.SongListAdapter
import com.nafanya.mp3world.features.allSongs.SongListViewModel
import com.nafanya.mp3world.features.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.features.playlists.playlist.Playlist
import javax.inject.Inject

class FavouriteListActivity : RecyclerHolderActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    override fun setViewModel() {
        viewModel = factory.create(SongListViewModel::class.java)
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
        (application as PlayerApplication).applicationComponent.favouritesComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setTitle() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        val observer = Observer<String> {
            supportActionBar?.title = it
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        (viewModel as SongListViewModel).title.observe(this, observer)
    }

    override fun onStart() {
        super.onStart()
//        SourceProvider.newInstanceWithPlaylist(
//            Playlist(
//                name = "Избранное",
//                id = 0,
//                songList = FavouriteListManager.favorites.value!!
//            )
//        )
//        (viewModel as SongListViewModel).start()
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptySongList.emptySongList.visibility = View.VISIBLE
    }
}
