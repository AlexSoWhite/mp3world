package com.nafanya.mp3world.features.artists.view

import android.os.Bundle
import android.view.LayoutInflater
import com.nafanya.mp3world.core.view.BaseActivity
import com.nafanya.mp3world.databinding.ActivityArtistsLayoutBinding
import com.r0adkll.slidr.Slidr

class ArtistListActivity : BaseActivity<ActivityArtistsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityArtistsLayoutBinding {
        return ActivityArtistsLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Slidr.attach(this)
    }
}
