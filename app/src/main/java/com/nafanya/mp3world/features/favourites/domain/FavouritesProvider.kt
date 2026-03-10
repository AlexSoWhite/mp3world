package com.nafanya.mp3world.features.favourites.domain

import com.nafanya.mp3world.core.list_managers.PlaylistProvider
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import kotlinx.coroutines.flow.Flow

interface FavouritesProvider : PlaylistProvider {

    val favorites: Flow<PlaylistWrapper>

    fun observeIsSongInFavorites(song: LocalSong): Flow<Boolean>

    suspend fun add(song: LocalSong)

    suspend fun delete(song: LocalSong)
}
