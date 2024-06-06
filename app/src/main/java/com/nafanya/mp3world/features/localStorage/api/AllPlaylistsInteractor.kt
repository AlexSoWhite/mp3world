package com.nafanya.mp3world.features.localStorage.api

import com.nafanya.mp3world.features.allPlaylists.model.PlaylistSongsEntity
import com.nafanya.mp3world.features.allPlaylists.model.PlaylistStorageEntity
import com.nafanya.mp3world.features.allPlaylists.model.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

interface AllPlaylistsInteractor {

    fun getAllPlaylists(): Flow<List<PlaylistWithSongs>>

    suspend fun insert(entity: PlaylistStorageEntity)

    suspend fun insertSongs(songs: List<PlaylistSongsEntity>)

    suspend fun update(entity: PlaylistStorageEntity)

    suspend fun update(
        oldEntity: PlaylistStorageEntity,
        newEntity: Pair<PlaylistStorageEntity, List<PlaylistSongsEntity>>
    )

    suspend fun delete(entity: PlaylistStorageEntity)
}
