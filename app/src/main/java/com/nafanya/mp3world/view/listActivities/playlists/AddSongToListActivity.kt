package com.nafanya.mp3world.view.listActivities.playlists

import android.view.Menu
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
import com.nafanya.mp3world.viewmodel.listViewModels.playlists.AddSongToListViewModel

class AddSongToListActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[AddSongToListViewModel::class.java]
    }

    override fun setAdapter() {
        val observer = Observer<Playlist> {
            binding.recycler.adapter = AddToPlaylistAdapter(
                viewModel as AddSongToListViewModel,
                it.songList,
                this
            ) {
                ForegroundServiceLiveDataProvider.currentPlaylist.value = it
            }
        }
        (viewModel as AddSongToListViewModel).playlist.observe(this, observer)
    }

    class AddToPlaylistAdapter(
        private val viewModel: AddSongToListViewModel,
        list: MutableList<Song>,
        context: LifecycleOwner,
        callback: () -> Unit
    ) : SongListAdapter(list, context, callback) {

        override fun decorateItem(binding: SongListItemBinding, song: Song) {
            viewModel.pendingPlaylist.value?.songList?.forEach {
                Glide.with(binding.action).load(R.drawable.done).into(binding.action)
                if (it.id == song.id) return
            }
            binding.action.visibility = View.VISIBLE
            Glide.with(binding.action).load(R.drawable.add).into(binding.action)
            var isAdded = false
            binding.action.setOnClickListener {
                isAdded = if (!isAdded) {
                    viewModel.addSong(song)
                    Glide.with(binding.action).load(R.drawable.done).into(binding.action)
                    true
                } else {
                    viewModel.deleteSong(song)
                    Glide.with(binding.action).load(R.drawable.add).into(binding.action)
                    false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_songs_to_playlist_menu, menu)
        val confirm = menu?.findItem(R.id.confirm_adding)
        confirm?.setOnMenuItemClickListener {
            ForegroundServiceLiveDataProvider.currentPlaylist.value = (viewModel as AddSongToListViewModel).pendingPlaylist.value
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptySongList.emptySongList.visibility = View.VISIBLE
    }
}
