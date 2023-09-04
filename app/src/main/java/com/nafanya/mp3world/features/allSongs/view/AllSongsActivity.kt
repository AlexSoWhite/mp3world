package com.nafanya.mp3world.features.allSongs.view

import android.view.LayoutInflater
import com.nafanya.mp3world.core.commonUi.BaseActivity
import com.nafanya.mp3world.databinding.ActivityAllSongsLayoutBinding

class AllSongsActivity : BaseActivity<ActivityAllSongsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityAllSongsLayoutBinding {
        return ActivityAllSongsLayoutBinding.inflate(layoutInflater)
    }
}
