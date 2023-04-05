package com.nafanya.mp3world.features.allPlaylists

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.nafanya.mp3world.core.mediaStore.MediaStoreInteractor
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.allPlaylists.model.PlaylistWithSongs
import com.nafanya.mp3world.features.allPlaylists.model.toStorageEntity
import com.nafanya.mp3world.features.localStorage.DatabaseHolder
import javax.inject.Inject
import kotlinx.coroutines.flow.combine

class AllPlaylistsLocalStorageInteractor @Inject constructor(
    private val dbHolder: DatabaseHolder,
    mediaStoreInteractor: MediaStoreInteractor,
) {

    private val mAllPlaylists = MutableLiveData<List<PlaylistWithSongs>>()
    val allPlaylists: LiveData<List<PlaylistWrapper>> = combine(
        mAllPlaylists.asFlow(),
        mediaStoreInteractor.allSongs
    ) { playlists, songs ->
        val list = mutableListOf<PlaylistWrapper>()
        playlists.sortedByDescending {
            it.playlistEntity.position
        }.forEach {
            val songList = mutableListOf<SongWrapper>()
            it.songEntities.forEach { entity ->
                songs?.firstOrNull { song ->
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
                    image = if (songList.isNotEmpty()) songList[0].art else null
                )
            )
        }
        list
    }.asLiveData()

    suspend fun getAll() {
        mAllPlaylists.postValue(dbHolder.db.playlistDao().getAll())
    }

    suspend fun insert(playlistWrapper: PlaylistWrapper) {
        val playlistEntity = playlistWrapper.toStorageEntity()
        dbHolder.db.playlistDao().apply {
            insert(playlistEntity.first)
            insertSongs(playlistEntity.second)
        }
        getAll()
    }

    suspend fun update(
        oldPlaylist: PlaylistWrapper,
        newPlaylist: PlaylistWrapper
    ) {
        val oldEntity = oldPlaylist.toStorageEntity()
        val newEntity = newPlaylist.toStorageEntity()
        dbHolder.db.playlistDao().apply {
            delete(oldEntity.first)
            insert(newEntity.first)
            insertSongs(newEntity.second)
        }
        getAll()
    }

    suspend fun updateOnlyPlaylists(
        playlist: PlaylistWrapper
    ) {
        dbHolder.db.playlistDao().update(playlist.toStorageEntity().first)
        getAll()
    }

    suspend fun delete(playlist: PlaylistWrapper) {
        dbHolder.db.playlistDao().delete(playlist.toStorageEntity().first)
        getAll()
    }
}
