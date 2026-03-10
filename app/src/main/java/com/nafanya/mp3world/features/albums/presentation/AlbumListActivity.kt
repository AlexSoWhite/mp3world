package com.nafanya.mp3world.features.albums.presentation

import android.view.LayoutInflater
import com.nafanya.mp3world.presentation.common_ui.BaseActivity
import com.nafanya.mp3world.databinding.ActivityAlbumsLayoutBinding

class AlbumListActivity : BaseActivity<ActivityAlbumsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityAlbumsLayoutBinding {
        return ActivityAlbumsLayoutBinding.inflate(layoutInflater)
    }
}
