package com.nafanya.mp3world.features.allPlaylists.view.mutablePlaylist

import android.os.Bundle
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.view.BaseActivity
import com.nafanya.mp3world.databinding.ActivityMutablePlaylistLayoutBinding
import com.r0adkll.slidr.Slidr

class MutablePlaylistActivity : BaseActivity<ActivityMutablePlaylistLayoutBinding>() {

    companion object {
        const val PLAYLIST_ID = "PLAYLIST_ID"
    }

    override fun inflate(layoutInflater: LayoutInflater): ActivityMutablePlaylistLayoutBinding {
        return ActivityMutablePlaylistLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Slidr.attach(this)
        val arguments = Bundle()
        arguments.putLong(PLAYLIST_ID, intent.getLongExtra(PLAYLIST_ID, -1))
        val fragment = MutablePlaylistFragment()
        fragment.arguments = arguments
        supportFragmentManager.beginTransaction()
            .replace(R.id.mutable_playlist_fragment_container, fragment)
            .commit()
    }
}
