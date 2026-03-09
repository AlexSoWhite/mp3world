package com.nafanya.mp3world.features.artists.presentation

import android.view.LayoutInflater
import com.nafanya.mp3world.presentation.commonUi.BaseActivity
import com.nafanya.mp3world.databinding.ActivityArtistsLayoutBinding

class ArtistListActivity : BaseActivity<ActivityArtistsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityArtistsLayoutBinding {
        return ActivityArtistsLayoutBinding.inflate(layoutInflater)
    }
}
