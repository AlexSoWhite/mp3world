package com.nafanya.mp3world.presentation.user_playlists.view_playlists

import android.view.LayoutInflater
import com.nafanya.mp3world.presentation.core.common_ui.BaseActivity
import com.nafanya.mp3world.databinding.ActivityAllPlaylistsLayoutBinding

class AllPlaylistsActivity : BaseActivity<ActivityAllPlaylistsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityAllPlaylistsLayoutBinding {
        return ActivityAllPlaylistsLayoutBinding.inflate(layoutInflater)
    }
}
