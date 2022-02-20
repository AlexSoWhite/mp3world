package com.nafanya.mp3world.view.listActivities.playlists

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.wrappers.Playlist

object PendingPlaylistProvider {

    val pendingPlaylist: MutableLiveData<Playlist> by lazy {
        MutableLiveData<Playlist>()
    }
}
