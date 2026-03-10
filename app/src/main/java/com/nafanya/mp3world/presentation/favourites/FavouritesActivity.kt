package com.nafanya.mp3world.presentation.favourites

import android.os.Bundle
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.list_managers.FAVOURITE_LIST_MANAGER_KEY
import com.nafanya.mp3world.presentation.core.common_ui.BaseActivity
import com.nafanya.mp3world.databinding.ActivityImmutablePlaylistLayoutBinding
import com.nafanya.mp3world.presentation.playlist.immutable_playlist.ImmutablePlaylistActivity.Companion.LIST_MANAGER_KEY
import com.nafanya.mp3world.presentation.playlist.immutable_playlist.ImmutablePlaylistFragment

class FavouritesActivity : BaseActivity<ActivityImmutablePlaylistLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityImmutablePlaylistLayoutBinding {
        return ActivityImmutablePlaylistLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = Bundle()
        arguments.putInt(LIST_MANAGER_KEY, FAVOURITE_LIST_MANAGER_KEY)
        val fragment = ImmutablePlaylistFragment()
        fragment.arguments = arguments
        supportFragmentManager.beginTransaction()
            .replace(R.id.immutable_playlist_fragment_container, fragment)
            .commit()
    }
}
