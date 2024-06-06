package com.nafanya.mp3world.features.allPlaylists.mutablePlaylist

import android.os.Bundle
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.commonUi.BaseActivity
import com.nafanya.mp3world.databinding.ActivityMutablePlaylistLayoutBinding

class MutablePlaylistActivity : BaseActivity<ActivityMutablePlaylistLayoutBinding>() {

    companion object {
        const val PLAYLIST_ID = "PLAYLIST_ID"
        const val PLAYLIST_NAME = "PLAYLIST_NAME"
    }

    override fun inflate(layoutInflater: LayoutInflater): ActivityMutablePlaylistLayoutBinding {
        return ActivityMutablePlaylistLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = Bundle()
        arguments.putLong(PLAYLIST_ID, intent.getLongExtra(PLAYLIST_ID, -1))
        arguments.putString(PLAYLIST_NAME, intent.getStringExtra(PLAYLIST_NAME))
        val fragment = MutablePlaylistFragment()
        fragment.arguments = arguments
        supportFragmentManager.beginTransaction()
            .replace(R.id.mutable_playlist_fragment_container, fragment)
            .commit()
    }
}
