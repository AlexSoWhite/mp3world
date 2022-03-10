package com.nafanya.mp3world.view.listActivities.search

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.SongListItemBinding
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListAdapter
import com.nafanya.mp3world.viewmodel.listViewModels.search.SearchSongListViewModel

class SearchSongListActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[SearchSongListViewModel::class.java]
    }

    override fun setAdapter() {
        val observer = Observer<Playlist> {
            binding.recycler.adapter = SearchResultAdapter(
                viewModel as SearchSongListViewModel,
                it.songList,
                this
            ) {
                ForegroundServiceLiveDataProvider.currentPlaylist.value = it
            }
        }
        (viewModel as SearchSongListViewModel).playlist.observe(this, observer)
    }

    class SearchResultAdapter(
        private val viewModel: SearchSongListViewModel,
        list: MutableList<Song>,
        context: LifecycleOwner,
        callback: () -> Unit
    ) : SongListAdapter(list, context, callback) {

        override fun decorateItem(binding: SongListItemBinding, song: Song) {
            binding.dateHolder.visibility = View.GONE
            var isAdded = viewModel.isAdded(song)
            binding.action.visibility = View.VISIBLE
            if (isAdded) {
                Glide.with(binding.action).load(R.drawable.done).into(binding.action)
            } else {
                Glide.with(binding.action).load(R.drawable.add).into(binding.action)
            }
            binding.action.setOnClickListener {
                isAdded = if (!isAdded) {
                    viewModel.addSongToLocalStorage(song)
                    Glide.with(binding.action).load(R.drawable.done).into(binding.action)
                    true
                } else {
                    viewModel.deleteSongFromLocalStorage(song)
                    Glide.with(binding.action).load(R.drawable.add).into(binding.action)
                    false
                }
            }
        }
    }
}
