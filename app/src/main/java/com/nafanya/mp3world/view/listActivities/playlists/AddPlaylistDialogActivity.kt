package com.nafanya.mp3world.view.listActivities.playlists

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.AddPlaylistDialogBinding
import com.nafanya.mp3world.viewmodel.listViewModels.playlists.PlaylistListViewModel

class AddPlaylistDialogActivity : AppCompatActivity() {

    private lateinit var binding: AddPlaylistDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.add_playlist_dialog)
        binding.input.setOnKeyListener(
            View.OnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    val text = binding.input.text.toString()
                    if (text.isBlank() || text.isEmpty()) {
                        Toast.makeText(
                            this,
                            "название плейлиста не может быть пустым",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.addEmptyPlaylistWithName(text) {
                            /*
                            val intent = Intent(this, AddSongToListActivity::class.java)
                            AddSongToListActivity.newInstance(it)
                            startActivity(intent)
                             */
                            finish()
                        }
                    }
                    return@OnKeyListener true
                }
                false
            }
        )
    }

    companion object {

        private lateinit var viewModel: PlaylistListViewModel

        fun setViewModel(viewModel: PlaylistListViewModel) {
            this.viewModel = viewModel
        }
    }
}
