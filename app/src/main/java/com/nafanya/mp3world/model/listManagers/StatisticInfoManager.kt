package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.localStorage.SongStatisticDao
import com.nafanya.mp3world.model.wrappers.SongStatisticEntity

object StatisticInfoManager {

    val info: MutableLiveData<MutableList<SongStatisticEntity>> by lazy {
        MutableLiveData<MutableList<SongStatisticEntity>>()
    }

    fun initialize(songStatisticDao: SongStatisticDao) {
        info.postValue(songStatisticDao.getAll())
    }
}
