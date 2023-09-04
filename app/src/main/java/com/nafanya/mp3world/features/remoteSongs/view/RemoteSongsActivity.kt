package com.nafanya.mp3world.features.remoteSongs.view

import android.os.Bundle
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.commonUi.BaseActivity
import com.nafanya.mp3world.databinding.ActivityRemoteSongsLayoutBinding

class RemoteSongsActivity : BaseActivity<ActivityRemoteSongsLayoutBinding>() {

    companion object {
        const val QUERY = "query"
    }

    override fun inflate(layoutInflater: LayoutInflater): ActivityRemoteSongsLayoutBinding {
        return ActivityRemoteSongsLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = Bundle()
        arguments.putString(QUERY, intent.getStringExtra(QUERY))
        val fragment = RemoteSongsFragment()
        fragment.arguments = arguments
        supportFragmentManager.beginTransaction()
            .replace(R.id.remote_songs_fragment_container, fragment)
            .commit()
    }
}
