package com.nafanya.mp3world.features.allPlaylists.allPlaylists

import android.view.LayoutInflater
import com.nafanya.mp3world.core.commonUi.BaseActivity
import com.nafanya.mp3world.databinding.ActivityAllPlaylistsLayoutBinding

class AllPlaylistsActivity : BaseActivity<ActivityAllPlaylistsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityAllPlaylistsLayoutBinding {
        return ActivityAllPlaylistsLayoutBinding.inflate(layoutInflater)
    }
}
