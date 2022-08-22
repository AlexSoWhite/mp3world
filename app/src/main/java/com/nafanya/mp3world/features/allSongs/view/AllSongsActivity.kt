package com.nafanya.mp3world.features.allSongs.view

import android.os.Bundle
import android.view.LayoutInflater
import com.nafanya.mp3world.core.view.BaseActivity
import com.nafanya.mp3world.databinding.ActivityAllSongsLayoutBinding
import com.r0adkll.slidr.Slidr

class AllSongsActivity : BaseActivity<ActivityAllSongsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityAllSongsLayoutBinding {
        return ActivityAllSongsLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Slidr.attach(this)
    }
}
