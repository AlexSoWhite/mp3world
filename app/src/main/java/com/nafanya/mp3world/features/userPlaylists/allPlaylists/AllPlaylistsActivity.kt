package com.nafanya.mp3world.features.userPlaylists.allPlaylists

import android.view.LayoutInflater
import com.nafanya.mp3world.presentation.commonUi.BaseActivity
import com.nafanya.mp3world.databinding.ActivityAllPlaylistsLayoutBinding

class AllPlaylistsActivity : BaseActivity<ActivityAllPlaylistsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityAllPlaylistsLayoutBinding {
        return ActivityAllPlaylistsLayoutBinding.inflate(layoutInflater)
    }
}
