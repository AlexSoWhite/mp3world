package com.nafanya.mp3world.features.playlists.playlistsList.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.DeletePlaylistDialogBinding
import com.nafanya.player.Playlist
import com.nafanya.mp3world.features.playlists.playlistsList.viewModel.PlaylistListViewModel

class DeletePlaylistDialogActivity : AppCompatActivity() {

    private lateinit var binding: DeletePlaylistDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.delete_playlist_dialog)
        binding.message.text = getString(R.string.delete_playlist, playlist.name)
        binding.delete.setOnClickListener {
            viewModel.deletePlaylist(playlist)
            finish()
        }
        binding.dismiss.setOnClickListener {
            finish()
        }
    }

    companion object {

        private lateinit var playlist: com.nafanya.player.Playlist
        private lateinit var viewModel: PlaylistListViewModel

        fun prepare(
            viewModel: PlaylistListViewModel,
            playlist: com.nafanya.player.Playlist
        ) {
            Companion.playlist = playlist
            Companion.viewModel = viewModel
        }
    }
}
