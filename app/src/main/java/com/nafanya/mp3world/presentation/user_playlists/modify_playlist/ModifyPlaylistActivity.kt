package com.nafanya.mp3world.presentation.user_playlists.modify_playlist

import android.os.Bundle
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.presentation.core.common_ui.BaseActivity
import com.nafanya.mp3world.databinding.ActivityModifyPlaylistLayoutBinding

class ModifyPlaylistActivity : BaseActivity<ActivityModifyPlaylistLayoutBinding>() {

    companion object {
        const val PLAYLIST_ID = "PLAYLIST_ID"
        const val PLAYLIST_NAME = "PLAYLIST_NAME"
    }

    override fun inflate(layoutInflater: LayoutInflater): ActivityModifyPlaylistLayoutBinding {
        return ActivityModifyPlaylistLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = Bundle()
        arguments.putLong(PLAYLIST_ID, intent.getLongExtra(PLAYLIST_ID, -1))
        arguments.putString(PLAYLIST_NAME, intent.getStringExtra(PLAYLIST_NAME))
        val fragment = ModifyPlaylistFragment()
        fragment.arguments = arguments
        supportFragmentManager.beginTransaction()
            .replace(R.id.modify_playlist_fragment_container, fragment)
            .commit()
    }
}
