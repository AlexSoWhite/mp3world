package com.nafanya.mp3world.features.artists.view

import android.view.LayoutInflater
import com.nafanya.mp3world.core.commonUi.BaseActivity
import com.nafanya.mp3world.databinding.ActivityArtistsLayoutBinding

class ArtistListActivity : BaseActivity<ActivityArtistsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityArtistsLayoutBinding {
        return ActivityArtistsLayoutBinding.inflate(layoutInflater)
    }
}
