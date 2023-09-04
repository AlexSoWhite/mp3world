package com.nafanya.mp3world.features.artists

import com.nafanya.mp3world.core.listManagers.ListManager
import kotlinx.coroutines.flow.Flow

interface ArtistListManager : ListManager {

    val artists: Flow<List<Artist>>
}
