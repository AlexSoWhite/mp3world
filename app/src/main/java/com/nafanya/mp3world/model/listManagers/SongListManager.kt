package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.localStorage.DatabaseHolder
import com.nafanya.mp3world.model.localStorage.SongDao
import com.nafanya.mp3world.model.wrappers.Song
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.concurrent.thread

object SongListManager {

    private var songListDao: SongDao? = null
    private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))

    val songList: MutableLiveData<MutableList<Song>> by lazy {
        MutableLiveData<MutableList<Song>>(mutableListOf())
    }
    var urlBasedCount = 0

    fun add(song: Song) {
        songList.value?.add(song)
    }

    fun appendLocalSongs(songListDao: SongDao) {
        val addition = songListDao.getAll()
        urlBasedCount = addition.size
        val newList = songList.value
        newList?.addAll(addition)
        songList.postValue(newList)
        songList.value?.sortByDescending { song ->
            simpleDateFormat.parse(song.date!!)
        }
    }

    fun addToStorage(song: Song) {
        thread {
            songList.value?.add(song)
            songList.value?.sortByDescending { song ->
                simpleDateFormat.parse(song.date!!)
            }
            songListDao?.insert(song)
            urlBasedCount++
        }
    }

    fun deleteFromStorage(song: Song) {
        thread {
            songList.value?.remove(song)
            songListDao?.delete(song)
            urlBasedCount--
        }
    }
}
