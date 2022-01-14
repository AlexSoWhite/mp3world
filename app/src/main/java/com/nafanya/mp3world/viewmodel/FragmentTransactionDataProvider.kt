package com.nafanya.mp3world.viewmodel

import androidx.lifecycle.MutableLiveData

object FragmentTransactionDataProvider {

    val fullScreenFragment: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
}
