package com.nafanya.mp3world.view.listActivities.search

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListAdapter
import com.nafanya.mp3world.viewmodel.listViewModels.search.SearchSongListViewModel

class SearchSongListActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[SearchSongListViewModel::class.java]
    }

    override fun setAdapter() {
        val observer = Observer<Playlist> {
            binding.recycler.adapter = SongListAdapter(
                it.songList,
                this
            ) {
                PlayerLiveDataProvider.currentPlaylist.value = it
            }
        }
        (viewModel as SearchSongListViewModel).playlist.observe(this, observer)
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptySearchResult.emptySearchResult.visibility = View.VISIBLE
    }
}
