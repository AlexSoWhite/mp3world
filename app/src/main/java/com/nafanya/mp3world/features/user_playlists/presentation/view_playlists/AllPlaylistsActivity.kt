package com.nafanya.mp3world.features.user_playlists.presentation.view_playlists

import android.view.LayoutInflater
import com.nafanya.mp3world.presentation.common_ui.BaseActivity
import com.nafanya.mp3world.databinding.ActivityAllPlaylistsLayoutBinding

class AllPlaylistsActivity : BaseActivity<ActivityAllPlaylistsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityAllPlaylistsLayoutBinding {
        return ActivityAllPlaylistsLayoutBinding.inflate(layoutInflater)
    }
}
