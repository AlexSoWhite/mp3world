package com.nafanya.mp3world.features.allPlaylists

import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import kotlinx.coroutines.flow.Flow

interface PlaylistListManager : ListManager {

    val playlists: Flow<List<PlaylistWrapper>>

    suspend fun addPlaylist(playlistName: String)

    suspend fun updatePlaylist(playlistWrapper: PlaylistWrapper)

    suspend fun deletePlaylist(playlistWrapper: PlaylistWrapper)
}
