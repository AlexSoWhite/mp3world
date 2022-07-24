package com.nafanya.mp3world.features.playlists.playlist.view

import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.PlaylistPopupActivityBinding
import com.nafanya.mp3world.features.allSongs.SongListAdapter
import com.nafanya.mp3world.features.foregroundService.PlayerLiveDataProvider

// TODO redesign it
class CurrentPlaylistDialogActivity : AppCompatActivity() {

    private lateinit var binding: PlaylistPopupActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.playlist_popup_activity)
        val attrs = window.attributes
        attrs.gravity = Gravity.BOTTOM
        attrs.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = attrs
        binding.playlist.layoutManager = LinearLayoutManager(this)
        val playlist = PlayerLiveDataProvider.currentPlaylist.value
        binding.playlist.adapter = SongListAdapter(
            playlist!!.songList,
            this,
        ) {
            PlayerLiveDataProvider.currentPlaylist.value = playlist
        }
        binding.playlistTitle.text = playlist.name
        OnSwipeListener(binding.playlist) {
            finish()
        }
    }
}
