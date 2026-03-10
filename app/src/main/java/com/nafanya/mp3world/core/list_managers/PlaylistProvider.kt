package com.nafanya.mp3world.core.list_managers

import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import kotlinx.coroutines.flow.Flow

interface PlaylistProvider {

    fun getPlaylistByContainerId(id: Long): Flow<PlaylistWrapper>
}
