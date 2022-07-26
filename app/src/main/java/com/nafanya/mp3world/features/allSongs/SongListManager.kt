package com.nafanya.mp3world.features.allSongs

import androidx.lifecycle.MutableLiveData
import com.nafanya.player.Song

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

    fun searchForSongs(query: String): List<Song> {
        val result = mutableListOf<Song>()
        songList.value!!.forEach {
            if (
                it.title!!.lowercase().contains(query.lowercase()) ||
                it.artist!!.lowercase().contains(query.lowercase())
            ) {
                result.add(it)
            }
        }
        return result
    }
}
