package com.nafanya.mp3world.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.model.Song

class SongListViewModel : ViewModel() {

    val currentSong: MutableLiveData<Song> by lazy {
        MutableLiveData<Song>()
    }
}
