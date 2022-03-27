package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object SongListManager {

    val songList: MutableLiveData<MutableList<Song>> by lazy {
        MutableLiveData<MutableList<Song>>(mutableListOf())
    }
    private var suspendedList = mutableListOf<Song>()
    var urlBasedCount: Long = 0

    fun add(song: Song) {
        suspendedList.add(song)
    }

    @DelicateCoroutinesApi
    fun resetData() {
        songList.value = suspendedList
        songList.value?.sortByDescending {
            it.date
        }
        if (songList.value!!.isNotEmpty()) {
            PlayerLiveDataProvider.currentPlaylist.value = Playlist(
                id = -1,
                name = "Мои песни",
                songList = songList.value!!
            )
        }
        GlobalScope.launch {
            suspendedList = mutableListOf()
            songList.value?.forEach {
                suspendedList.add(it)
            }
        }
    }
}
