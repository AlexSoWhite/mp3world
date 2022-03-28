package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.wrappers.Song
/**
 * Object that holds all song data.
 * The most important manager, favourites and playlists are populated based on its data.
 */
object SongListManager {

    val songList: MutableLiveData<MutableList<Song>> by lazy {
        MutableLiveData<MutableList<Song>>(mutableListOf())
    }
    private var suspendedList = mutableListOf<Song>()
    var urlBasedCount: Long = 0

    fun add(song: Song) {
        suspendedList.add(song)
    }

    fun resetData() {
        suspendedList.sortByDescending {
            it.date
        }
        if (suspendedList.isNotEmpty()) {
            songList.postValue(suspendedList)
        }
        suspendedList = mutableListOf()
    }

    fun resetDataOnMainThread() {
        suspendedList.sortByDescending {
            it.date
        }
        if (suspendedList.isNotEmpty()) {
            songList.value = suspendedList
        }
        suspendedList = mutableListOf()
    }
}
