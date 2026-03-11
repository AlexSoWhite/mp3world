package com.nafanya.mp3world.presentation.all_songs

import android.view.LayoutInflater
import com.nafanya.mp3world.presentation.core.common_ui.BaseActivity
import com.nafanya.mp3world.databinding.ActivityAllSongsLayoutBinding

class AllSongsActivity : BaseActivity<ActivityAllSongsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityAllSongsLayoutBinding {
        return ActivityAllSongsLayoutBinding.inflate(layoutInflater)
    }
}
