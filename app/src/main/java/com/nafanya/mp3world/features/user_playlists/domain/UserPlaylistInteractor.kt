package com.nafanya.mp3world.features.user_playlists.domain

import com.nafanya.mp3world.core.list_managers.PlaylistProvider
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import kotlinx.coroutines.flow.Flow

interface UserPlaylistInteractor : PlaylistProvider {

    val playlists: Flow<List<PlaylistWrapper>>

    suspend fun addPlaylist(playlistName: String)

    suspend fun updatePlaylist(playlistWrapper: PlaylistWrapper)

    suspend fun deletePlaylist(playlistWrapper: PlaylistWrapper)
}
