package com.nafanya.mp3world.core.entrypoint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Initializer {

    private val mInitializationLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val initializationLiveData: LiveData<Boolean>
        get() = mInitializationLiveData

    suspend fun initialize() = withContext(Dispatchers.Main) {
        mInitializationLiveData.value = true
    }
}
