package com.nafanya.mp3world.features.allPlaylists.view.allPlaylists

import android.os.Bundle
import android.view.LayoutInflater
import com.nafanya.mp3world.core.view.BaseActivity
import com.nafanya.mp3world.databinding.ActivityAllPlaylistsLayoutBinding
import com.r0adkll.slidr.Slidr

class AllPlaylistsActivity : BaseActivity<ActivityAllPlaylistsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityAllPlaylistsLayoutBinding {
        return ActivityAllPlaylistsLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Slidr.attach(this)
    }
}
