package com.nafanya.mp3world.features.playlist.immutablePlaylist

import android.os.Bundle
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.view.BaseActivity
import com.nafanya.mp3world.databinding.ActivityImmutablePlaylistLayoutBinding
import com.r0adkll.slidr.Slidr

class ImmutablePlaylistActivity : BaseActivity<ActivityImmutablePlaylistLayoutBinding>() {

    companion object {
        const val LIST_MANAGER_KEY = "LIST_MANAGER_KEY"
        const val CONTAINER_ID = "CONTAINER_ID"
    }

    override fun inflate(layoutInflater: LayoutInflater): ActivityImmutablePlaylistLayoutBinding {
        return ActivityImmutablePlaylistLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Slidr.attach(this)
        val arguments = Bundle()
        arguments.putInt(LIST_MANAGER_KEY, intent.getIntExtra(LIST_MANAGER_KEY, -1))
        arguments.putLong(CONTAINER_ID, intent.getLongExtra(CONTAINER_ID, -1))
        val fragment = ImmutablePlaylistFragment()
        fragment.arguments = arguments
        supportFragmentManager.beginTransaction()
            .replace(R.id.immutable_playlist_fragment_container, fragment)
            .commit()
    }
}
