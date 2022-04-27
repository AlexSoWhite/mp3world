package com.nafanya.mp3world.view.listActivities.playlists

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.DeletePlaylistDialogBinding
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.viewmodel.listViewModels.playlists.PlaylistListViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

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

        private lateinit var playlist: Playlist
        private lateinit var viewModel: PlaylistListViewModel

        fun prepare(
            viewModel: PlaylistListViewModel,
            playlist: Playlist
        ) {
            this.playlist = playlist
            this.viewModel = viewModel
        }
    }
}
