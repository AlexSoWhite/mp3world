package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.localStorage.SongListDao
import com.nafanya.mp3world.model.wrappers.Song
import kotlin.concurrent.thread

object SongListManager {

    private lateinit var songListDao: SongListDao
    val songList: MutableLiveData<MutableList<Song>> by lazy {
        MutableLiveData<MutableList<Song>>(mutableListOf())
    }
    var urlBasedCount = 0

    fun add(song: Song) {
        songList.value?.add(song)
    }

    fun appendLocalSongs() {
        thread {
            val addition = songListDao.getAll()
            urlBasedCount = addition.size
            songList.value?.addAll(addition)
        }
    }

    fun addToStorage(song: Song) {
        thread {
            songListDao.insert(song)
            urlBasedCount++
        }
    }

    fun deleteFromStorage(song: Song) {
        thread {
            songListDao.delete(song)
        }
    }
}
