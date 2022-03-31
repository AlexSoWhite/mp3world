package com.nafanya.mp3world.model.listManagers

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.localStorage.StoredPlaylistDao
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song

/**
 * Object that holds favourites data. Managed by DataBaseHolder and LocalStorageProvider.
 */
object PlaylistListManager {

    val playlists: MutableLiveData<MutableList<Playlist>> by lazy {
        MutableLiveData<MutableList<Playlist>>(mutableListOf())
    }

    @Suppress("NestedBlockDepth")
    fun initialize(playlistDao: StoredPlaylistDao) {
        val storedPlaylists = playlistDao.getAll()
        val temp = mutableListOf<Playlist>()
        var bitmap: Bitmap? = null
        storedPlaylists.forEach {
            val songList = mutableListOf<Song>()
            it.songIds?.forEach { id ->
                SongListManager.songList.value?.forEach { song ->
                    if (id == song.id) {
                        songList.add(song)
                        if (song.art != null) {
                            bitmap = song.art
                        }
                    }
                }
            }
            temp.add(Playlist(songList, it.id, it.name, bitmap))
        }
        playlists.postValue(temp)
    }

    fun addPlaylist(playlist: Playlist) {
        val temp = playlists.value
        temp?.add(playlist)
        playlists.value = temp
    }

    fun updatePlaylist(playlist: Playlist) {
        val index = playlists.value!!.indexOf(playlist)
        if (index != -1) {
            playlist.songList.forEach {
                if (it.art != null) {
                    playlist.image = it.art
                }
            }
            playlists.value!![index] = playlist
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        val temp = playlists.value
        temp?.remove(playlist)
        playlists.value = temp
    }
}
