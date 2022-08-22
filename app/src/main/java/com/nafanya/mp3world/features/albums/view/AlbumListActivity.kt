package com.nafanya.mp3world.features.albums.view

import android.view.LayoutInflater
import com.nafanya.mp3world.core.view.BaseActivity
import com.nafanya.mp3world.databinding.ActivityAlbumsLayoutBinding

class AlbumListActivity : BaseActivity<ActivityAlbumsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityAlbumsLayoutBinding {
        return ActivityAlbumsLayoutBinding.inflate(layoutInflater)
    }
}
