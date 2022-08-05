package com.nafanya.mp3world.features.playlists.playlistsList

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import com.nafanya.mp3world.features.localStorage.LocalStorageProvider
import com.nafanya.player.Playlist
import com.nafanya.player.Song
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Object that holds favourites data. Managed by DataBaseHolder and LocalStorageProvider.
 */
@Singleton
class PlaylistListManager @Inject constructor(
    private val localStorageProvider: LocalStorageProvider
) : ListManager {

    private val mPlaylists: MutableLiveData<List<Playlist>> = MutableLiveData(listOf())
    val playlists: LiveData<List<Playlist>>
        get() = mPlaylists

    @Suppress("NestedBlockDepth")
    override suspend fun populate(mediaStoreReader: MediaStoreReader) {
        withContext(Dispatchers.IO) {
            val storedPlaylists = localStorageProvider.dbHolder.db.playlistDao().getAll()
            val temp = mutableListOf<Playlist>()
            var bitmap: Bitmap? = null
            storedPlaylists.forEach { storedPlaylist ->
                val songList = mutableListOf<Song>()
                storedPlaylist.songIds?.forEach { id ->
                    mediaStoreReader.allSongs.forEach { song ->
                        if (id == song.id) {
                            songList.add(song)
                            if (song.art != null) {
                                bitmap = song.art
                            }
                        }
                    }
                }
                temp.add(Playlist(songList, storedPlaylist.id, storedPlaylist.name, bitmap))
            }
            mPlaylists.postValue(temp)
        }
    }

    fun addPlaylist(playlist: Playlist) {
        val temp = mPlaylists.value as MutableList<Playlist>
        temp.add(playlist)
        mPlaylists.value = temp
    }

    fun updatePlaylist(playlist: Playlist) {
        val playlists = mPlaylists.value as MutableList<Playlist>
        val index = playlists.indexOf(playlist)
        if (index != -1) {
            playlist.songList.forEach {
                if (it.art != null) {
                    playlist.image = it.art
                }
            }
            playlists[index] = playlist
            mPlaylists.postValue(playlists)
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        val temp = mPlaylists.value as MutableList<Playlist>
        temp.remove(playlist)
        mPlaylists.value = temp
    }
}
