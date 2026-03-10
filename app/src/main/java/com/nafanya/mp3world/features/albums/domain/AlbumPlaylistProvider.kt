package com.nafanya.mp3world.features.albums.domain

import com.nafanya.mp3world.core.list_managers.PlaylistProvider
import kotlinx.coroutines.flow.Flow

interface AlbumPlaylistProvider : PlaylistProvider {

    val albums: Flow<List<Album>>
}
