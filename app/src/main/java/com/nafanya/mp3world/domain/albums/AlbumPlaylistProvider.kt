package com.nafanya.mp3world.domain.albums

import com.nafanya.mp3world.core.list_managers.PlaylistProvider
import kotlinx.coroutines.flow.Flow

interface AlbumPlaylistProvider : PlaylistProvider {

    val albums: Flow<List<Album>>
}
