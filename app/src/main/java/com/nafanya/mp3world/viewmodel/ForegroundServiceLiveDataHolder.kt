package com.nafanya.mp3world.viewmodel

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.Playlist
import com.nafanya.mp3world.model.Song

object ForegroundServiceLiveDataHolder {

    val currentPlaylist: MutableLiveData<Playlist> by lazy {
        MutableLiveData<Playlist>()
    }

    val currentSong: MutableLiveData<Song> by lazy {
        MutableLiveData<Song>()
    }
}
