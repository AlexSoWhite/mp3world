package com.nafanya.mp3world.features.all_songs.presentation

import android.view.LayoutInflater
import com.nafanya.mp3world.presentation.common_ui.BaseActivity
import com.nafanya.mp3world.databinding.ActivityAllSongsLayoutBinding

class AllSongsActivity : BaseActivity<ActivityAllSongsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityAllSongsLayoutBinding {
        return ActivityAllSongsLayoutBinding.inflate(layoutInflater)
    }
}
