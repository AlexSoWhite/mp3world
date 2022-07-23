package com.nafanya.mp3world.features.statistics

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.features.localStorage.SongStatisticDao
import kotlinx.coroutines.runBlocking

/**
 * TODO: make it work
*/
object StatisticInfoManager {

    val info: MutableLiveData<MutableList<SongStatisticEntity>> by lazy {
        MutableLiveData<MutableList<SongStatisticEntity>>()
    }

    fun initialize(songStatisticDao: SongStatisticDao) = runBlocking {
        val list = songStatisticDao.getAll()
        list.sortByDescending {
            it.time
        }
        info.postValue(list)
    }
}
