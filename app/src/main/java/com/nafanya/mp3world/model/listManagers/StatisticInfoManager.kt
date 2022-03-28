package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.localStorage.SongStatisticDao
import com.nafanya.mp3world.model.wrappers.SongStatisticEntity

/**
 * TODO: make it work
*/
object StatisticInfoManager {

    val info: MutableLiveData<MutableList<SongStatisticEntity>> by lazy {
        MutableLiveData<MutableList<SongStatisticEntity>>()
    }

    fun initialize(songStatisticDao: SongStatisticDao) {
        val list = songStatisticDao.getAll()
        list.sortByDescending {
            it.time
        }
        info.postValue(list)
    }
}
