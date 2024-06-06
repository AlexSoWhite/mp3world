package com.nafanya.mp3world.features.favourites

import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import kotlinx.coroutines.flow.Flow

interface FavouritesManager : ListManager {

    val favorites: Flow<PlaylistWrapper>

    fun isSongInFavourites(song: LocalSong): Flow<Boolean>

    suspend fun add(song: LocalSong)

    suspend fun delete(song: LocalSong)
}
