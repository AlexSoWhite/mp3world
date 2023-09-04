package com.nafanya.mp3world.core.listManagers

import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import kotlinx.coroutines.flow.Flow

interface ListManager {

    fun getPlaylistByContainerId(id: Long): Flow<PlaylistWrapper>
}
