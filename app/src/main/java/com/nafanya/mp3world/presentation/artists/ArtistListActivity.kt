package com.nafanya.mp3world.presentation.artists

import android.view.LayoutInflater
import com.nafanya.mp3world.presentation.core.common_ui.BaseActivity
import com.nafanya.mp3world.databinding.ActivityArtistsLayoutBinding

class ArtistListActivity : BaseActivity<ActivityArtistsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityArtistsLayoutBinding {
        return ActivityArtistsLayoutBinding.inflate(layoutInflater)
    }
}
