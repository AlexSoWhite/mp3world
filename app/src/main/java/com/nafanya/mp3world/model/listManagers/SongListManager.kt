package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.model.localStorage.SongDao
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object SongListManager {

    private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))

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

    fun appendLocalSongs(songListDao: SongDao) {
        val addition = songListDao.getAll()
        val newList = songList.value
        newList?.addAll(addition)
        songList.postValue(newList)
        songList.value?.sortByDescending { song ->
            simpleDateFormat.parse(song.date!!)
        }
    }

    fun addSongWithUrl(song: Song) {
        val newList = songList.value
        newList?.add(song)
        newList?.sortByDescending { it ->
            simpleDateFormat.parse(it.date!!)
        }
        songList.value = newList
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
        // PlaylistListManager.deleteSongWithUrl(song)
        urlBasedCount--
    }
}
