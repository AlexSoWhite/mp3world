package com.nafanya.mp3world.features.user_playlists.domain

import android.net.Uri
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.coroutines.collectLatestInScope
import com.nafanya.mp3world.core.coroutines.emitInScope
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.data.user_playlists.toStorageEntity
import com.nafanya.mp3world.data.local_storage.api.UserPlaylistsRepository
import com.nafanya.mp3world.data.mediaStore.MediaStoreInteractor
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapNotNull

/**
 * Object that holds favourites data. Managed by DataBaseHolder and LocalStorageProvider.
 */
@Singleton
class PlaylistListManagerImpl @Inject constructor(
    private val userPlaylistsRepository: UserPlaylistsRepository,
    private val ioCoroutineProvider: IOCoroutineProvider,
    mediaStoreInteractor: MediaStoreInteractor
) : PlaylistListManager {

    private val mPlaylists = MutableSharedFlow<List<PlaylistWrapper>>(replay = 1)
    override val playlists: Flow<List<PlaylistWrapper>>
        get() = mPlaylists

    /**
     * If replay cache is empty, this will return empty list. Seems legit as we are setting
     * it to empty list eventually if there are no playlists. Methods that calls for it
     * should do it only after list was initialized
     *
     * TODO autogenerate necessary parameters to move it out of software responsibility
     */
    private val snapshot: List<PlaylistWrapper>
        get() = mPlaylists.replayCache[0]

    init {
        mediaStoreInteractor.allSongs.collectLatestInScope(ioCoroutineProvider.ioScope) { songs ->
            userPlaylistsRepository.observeUserPlaylists().collectLatestInScope(
                ioCoroutineProvider.ioScope
            ) { entries ->
                val list = mutableListOf<PlaylistWrapper>()
                entries.sortedByDescending {
                    it.playlistEntity.position
                }.forEach {
                    val songList = mutableListOf<SongWrapper>()
                    it.songEntities.forEach { entity ->
                        songs.firstOrNull { song ->
                            Uri.parse(entity.uri) == song.uri
                        }?.let { localSong ->
                            songList.add(localSong)
                        }
                    }
                    list.add(
                        PlaylistWrapper(
                            songList = songList,
                            id = it.playlistEntity.id,
                            name = it.playlistEntity.name,
                            position = it.playlistEntity.position,
                            imageSource = songList.firstOrNull()
                        )
                    )
                }
                mPlaylists.emitInScope(ioCoroutineProvider.ioScope, list)
            }
        }
    }

    override fun getPlaylistByContainerId(id: Long): Flow<PlaylistWrapper> {
        return playlists.mapNotNull {
            it.firstOrNull { playlist -> playlist.id == id }
        }
    }

    override suspend fun addPlaylist(playlistName: String) {
        val entity = PlaylistWrapper(
            name = playlistName,
            id = snapshot.maxOfOrNull { it.id + 1 } ?: 0,
            songList = listOf(),
            position = snapshot.maxOfOrNull { it.position + 1 } ?: 0
        ).toStorageEntity()
        userPlaylistsRepository.apply {
            insert(entity.first)
            insertSongs(entity.second)
        }
    }

    override suspend fun updatePlaylist(playlistWrapper: PlaylistWrapper) {
        val temp = mutableListOf<PlaylistWrapper>()
        snapshot.forEach {
            temp.add(it)
        }
        playlistWrapper.songList.forEach {
            playlistWrapper.imageSource = it
        }
        val index = temp.indexOf(playlistWrapper)
        if (index != -1) {
            val oldEntity = temp[index].toStorageEntity()
            val newEntity = playlistWrapper.toStorageEntity()
            userPlaylistsRepository.update(oldEntity.first, newEntity)
        }
    }

    override suspend fun deletePlaylist(playlistWrapper: PlaylistWrapper) {
        userPlaylistsRepository.delete(playlistWrapper.toStorageEntity().first)
    }
}
