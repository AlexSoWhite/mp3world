package com.nafanya.mp3world.model.listManagers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.localStorage.SongDao
import com.nafanya.mp3world.model.wrappers.Song
import java.text.SimpleDateFormat
import java.util.Locale

object SongListManager {

    private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))

    val songList: MutableLiveData<MutableList<Song>> by lazy {
        MutableLiveData<MutableList<Song>>(mutableListOf())
    }
    var urlBasedCount: Long = 0

    fun add(song: Song) {
        songList.value?.add(song)
    }

    fun appendLocalSongs(songListDao: SongDao) {
        val addition = songListDao.getAll()
        urlBasedCount = addition.size.toLong()
        val newList = songList.value
        newList?.addAll(addition)
        songList.postValue(newList)
        songList.value?.sortByDescending { song ->
            simpleDateFormat.parse(song.date!!)
        }
    }

    fun addSongWithUrl(song: Song) {
        val newList = songList.value
        song.id = urlBasedCount
        newList?.add(song)
        newList?.sortByDescending { it ->
            simpleDateFormat.parse(it.date!!)
        }
        songList.value = newList
        urlBasedCount++
    }

    fun deleteSongWithUrl(song: Song) {
        val newList = songList.value
        for (i in newList!!.indices) {
            if (newList[i].url == song.url) {
                newList.removeAt(i)
                break
            }
        }
        newList.sortByDescending {
            simpleDateFormat.parse(it.date!!)
        }
        songList.value = newList
        urlBasedCount--
        Log.d("SONG", "deleted from live data")
    }
}
