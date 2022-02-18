package com.nafanya.mp3world.view.listActivities.playlists

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.AddPlaylistDialogBinding
import com.nafanya.mp3world.viewmodel.PlaylistListViewModel

class AddPlaylistDialogActivity : AppCompatActivity() {

    private lateinit var binding: AddPlaylistDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.add_playlist_dialog)
        val viewModel = ViewModelProvider(this)[PlaylistListViewModel::class.java]
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
                        viewModel.addEmptyPlaylistWithName(text)
                        finish()
                    }
                    return@OnKeyListener true
                }
                false
            }
        )
    }
}
