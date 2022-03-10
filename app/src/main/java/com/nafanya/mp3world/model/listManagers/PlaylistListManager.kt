package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.localStorage.PlaylistDao
import com.nafanya.mp3world.model.wrappers.Playlist

object PlaylistListManager {

    val playlists: MutableLiveData<MutableList<Playlist>> by lazy {
        MutableLiveData<MutableList<Playlist>>(mutableListOf())
    }

    fun initialize(playlistDao: PlaylistDao) {
        playlists.postValue(playlistDao.getAll())
    }

    fun addPlaylist(playlist: Playlist) {
        playlists.value?.add(playlist)
    }

    fun updatePlaylist(playlist: Playlist) {
        val index = playlists.value!!.indexOf(playlist)
        if (index != -1) {
            playlists.value!![index] = playlist
        }
    }

    fun getPlaylistByName(name: String): Playlist? {
        playlists.value?.forEach {
            if (it.name == name) return it
        }
        return null
    }
}
