package com.nafanya.mp3world.features.allPlaylists

import android.net.Uri
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.coroutines.collectLatestInScope
import com.nafanya.mp3world.core.coroutines.emitInScope
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.features.allPlaylists.model.toStorageEntity
import com.nafanya.mp3world.features.localStorage.api.AllPlaylistsInteractor
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.mapNotNull

/**
 * Object that holds favourites data. Managed by DataBaseHolder and LocalStorageProvider.
 */
@Singleton
class PlaylistListManager @Inject constructor(
    private val allPlaylistsInteractor: AllPlaylistsInteractor,
    private val ioCoroutineProvider: IOCoroutineProvider,
    mediaStoreInteractor: MediaStoreInteractor
) : ListManager {

    private val mPlaylists = MutableSharedFlow<List<PlaylistWrapper>>(replay = 1)
    val playlists: SharedFlow<List<PlaylistWrapper>>
        get() = mPlaylists

    /**
     * If replay cache is empty, this will return empty list. Seems legit as we are setting
     * it to empty list eventually if there are no playlists. Methods that calls for it
     * should do it only after list was initialized
     *
     * TODO autogenerate necessary parameters to move it out of software responsibility
     */
    private val snapshot: List<PlaylistWrapper>
        get() = playlists.replayCache[0]

    init {
        mediaStoreInteractor.allSongs.collectLatestInScope(ioCoroutineProvider.ioScope) { songs ->
            allPlaylistsInteractor.getAllPlaylists().collectLatestInScope(
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

    suspend fun addPlaylist(playlistName: String) {
        val entity = PlaylistWrapper(
            name = playlistName,
            id = snapshot.maxOfOrNull { it.id + 1 } ?: 0,
            songList = listOf(),
            position = snapshot.maxOfOrNull { it.position + 1 } ?: 0
        ).toStorageEntity()
        allPlaylistsInteractor.apply {
            insert(entity.first)
            insertSongs(entity.second)
        }
    }

    suspend fun updatePlaylist(playlist: PlaylistWrapper) {
        val temp = mutableListOf<PlaylistWrapper>()
        snapshot.forEach {
            temp.add(it)
        }
        playlist.songList.forEach {
            playlist.imageSource = it
        }
        val index = temp.indexOf(playlist)
        if (index != -1) {
            val oldEntity = temp[index].toStorageEntity()
            val newEntity = playlist.toStorageEntity()
            allPlaylistsInteractor.update(oldEntity.first, newEntity)
        }
    }

    suspend fun deletePlaylist(playlist: PlaylistWrapper) {
        allPlaylistsInteractor.delete(playlist.toStorageEntity().first)
    }
}
