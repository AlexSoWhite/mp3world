package com.nafanya.mp3world.domain.artists

import com.nafanya.mp3world.core.list_managers.PlaylistProvider
import kotlinx.coroutines.flow.Flow

interface ArtistPlaylistProvider : PlaylistProvider {

    val artists: Flow<List<Artist>>
}
