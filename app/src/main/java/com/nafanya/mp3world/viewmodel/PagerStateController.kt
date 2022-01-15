package com.nafanya.mp3world.viewmodel

import androidx.lifecycle.MutableLiveData

object PagerStateController {

    val isPagingEnabled: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(true)
    }
}
