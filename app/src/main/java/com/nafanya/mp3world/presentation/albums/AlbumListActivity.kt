package com.nafanya.mp3world.presentation.albums

import android.view.LayoutInflater
import com.nafanya.mp3world.presentation.core.common_ui.BaseActivity
import com.nafanya.mp3world.databinding.ActivityAlbumsLayoutBinding

class AlbumListActivity : BaseActivity<ActivityAlbumsLayoutBinding>() {

    override fun inflate(layoutInflater: LayoutInflater): ActivityAlbumsLayoutBinding {
        return ActivityAlbumsLayoutBinding.inflate(layoutInflater)
    }
}
