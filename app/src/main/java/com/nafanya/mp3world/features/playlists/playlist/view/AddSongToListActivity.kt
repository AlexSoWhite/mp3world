package com.nafanya.mp3world.features.playlists.playlist.view

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.player.Song
import com.nafanya.mp3world.core.view.RecyclerHolderActivity
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.databinding.SongListItemBinding
import com.nafanya.mp3world.features.allSongs.SongListAdapter
import com.nafanya.player.Playlist
import com.nafanya.mp3world.features.playlists.playlist.viewModel.AddSongToListViewModel
import javax.inject.Inject

class AddSongToListActivity : RecyclerHolderActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    override fun setViewModel() {
        viewModel = factory.create(AddSongToListViewModel::class.java)
    }

    override fun setAdapter() {
        val observer = Observer<Playlist> {
            binding.recycler.adapter = AddToPlaylistAdapter(
                viewModel as AddSongToListViewModel,
                it
            ) { playlist, song ->
                viewModel.onClick(playlist, song)
            }
        }
        (viewModel as AddSongToListViewModel).playlist.observe(this, observer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PlayerApplication).applicationComponent.playlistComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    class AddToPlaylistAdapter(
        private val viewModel: AddSongToListViewModel,
        playlist: Playlist,
        callback: (Playlist, Song) -> Unit
    ) : SongListAdapter(playlist, null, callback) {

        override fun decorateItem(binding: SongListItemBinding, song: Song) {
            var isAdded = viewModel.isAdded(song)
            binding.action.visibility = View.VISIBLE
            if (isAdded) {
                Glide.with(binding.action).load(R.drawable.done).into(binding.action)
            } else {
                Glide.with(binding.action).load(R.drawable.add).into(binding.action)
            }
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_songs_to_playlist_menu, menu)
        val confirm = menu.findItem(R.id.confirm_adding)
        confirm?.setOnMenuItemClickListener {
            (viewModel as AddSongToListViewModel).confirmChanges()
            finish()
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptySongList.emptySongList.visibility = View.VISIBLE
    }
}
