package com.nafanya.mp3world.data.local_storage.api

import com.nafanya.mp3world.data.user_playlists.PlaylistSongsEntity
import com.nafanya.mp3world.data.user_playlists.PlaylistStorageEntity
import com.nafanya.mp3world.data.user_playlists.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

interface UserPlaylistsRepository {

    fun observeUserPlaylists(): Flow<List<PlaylistWithSongs>>

    suspend fun insert(entity: PlaylistStorageEntity)

    suspend fun insertSongs(songs: List<PlaylistSongsEntity>)

    suspend fun update(entity: PlaylistStorageEntity)

    suspend fun update(
        oldEntity: PlaylistStorageEntity,
        newEntity: Pair<PlaylistStorageEntity, List<PlaylistSongsEntity>>
    )

    suspend fun delete(entity: PlaylistStorageEntity)
}
