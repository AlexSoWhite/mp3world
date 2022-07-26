package com.nafanya.mp3world.features.playlists.playlist.view

import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.databinding.PlaylistPopupActivityBinding
import com.nafanya.mp3world.features.allSongs.SongListAdapter
import com.nafanya.mp3world.features.allSongs.SongListViewModel
import javax.inject.Inject

// TODO redesign it
class CurrentPlaylistDialogActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: SongListViewModel

    private lateinit var binding: PlaylistPopupActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PlayerApplication).applicationComponent.playlistComponent().inject(this)
        super.onCreate(savedInstanceState)
        viewModel = factory.create(SongListViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.playlist_popup_activity)
        val attrs = window.attributes
        attrs.gravity = Gravity.BOTTOM
        attrs.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = attrs
        binding.playlist.layoutManager = LinearLayoutManager(this)
        val playlist = viewModel.playlist.value
        binding.playlist.adapter = SongListAdapter(
            playlist!!,
            null,
        ) { playlist, song ->
            viewModel.onClick(playlist, song)
        }
        binding.playlistTitle.text = playlist.name
        OnSwipeListener(binding.playlist) {
            finish()
        }
    }
}
