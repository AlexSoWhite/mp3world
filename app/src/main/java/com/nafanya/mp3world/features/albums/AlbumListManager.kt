package com.nafanya.mp3world.features.albums

import com.nafanya.mp3world.core.listManagers.ListManager
import kotlinx.coroutines.flow.Flow

interface AlbumListManager : ListManager {

    val albums: Flow<List<Album>>
}
