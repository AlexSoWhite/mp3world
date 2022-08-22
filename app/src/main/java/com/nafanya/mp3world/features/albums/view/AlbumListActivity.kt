package com.nafanya.mp3world.features.albums.view

import android.os.Bundle
import android.view.LayoutInflater
import com.nafanya.mp3world.core.view.BaseActivity
import com.nafanya.mp3world.databinding.ActivityAlbumsLayoutBinding
import com.r0adkll.slidr.Slidr

class AlbumListActivity : BaseActivity<ActivityAlbumsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityAlbumsLayoutBinding {
        return ActivityAlbumsLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Slidr.attach(this)
    }
}
