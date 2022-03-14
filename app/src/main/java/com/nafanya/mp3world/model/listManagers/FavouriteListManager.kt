package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.localStorage.FavouriteListDao
import com.nafanya.mp3world.model.wrappers.Song

object FavouriteListManager {

    val songList: MutableLiveData<MutableList<Song>> by lazy {
        MutableLiveData<MutableList<Song>>()
    }

    fun add(song: Song) {
        val temp = songList.value
        temp?.add(song)
        songList.postValue(temp)
    }

    fun delete(song: Song) {
        val temp = songList.value
        temp?.remove(song)
        songList.postValue(temp)
    }

    fun initialize(dao: FavouriteListDao) {
        val ids = dao.getAll()
        val temp = mutableListOf<Song>()
        ids.forEach { id ->
            SongListManager.songList.value?.forEach { song ->
                if (id == song.id) {
                    temp.add(song)
                }
            }
        }
        songList.postValue(temp)
    }
}
