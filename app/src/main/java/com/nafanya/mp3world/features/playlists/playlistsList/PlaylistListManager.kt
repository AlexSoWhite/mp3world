package com.nafanya.mp3world.features.playlists.playlistsList

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.nafanya.player.Song
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.localStorage.StoredPlaylistDao
import com.nafanya.player.Playlist
import kotlinx.coroutines.runBlocking

/**
 * Object that holds favourites data. Managed by DataBaseHolder and LocalStorageProvider.
 */
object PlaylistListManager {

    val playlists: MutableLiveData<MutableList<com.nafanya.player.Playlist>> by lazy {
        MutableLiveData<MutableList<com.nafanya.player.Playlist>>(mutableListOf())
    }

    @Suppress("NestedBlockDepth")
    fun initialize(playlistDao: StoredPlaylistDao) = runBlocking {
        val storedPlaylists = playlistDao.getAll()
        val temp = mutableListOf<com.nafanya.player.Playlist>()
        var bitmap: Bitmap? = null
        storedPlaylists.forEach {
            val songList = mutableListOf<com.nafanya.player.Song>()
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
            temp.add(com.nafanya.player.Playlist(songList, it.id, it.name, bitmap))
        }
        playlists.postValue(temp)
    }

    fun addPlaylist(playlist: com.nafanya.player.Playlist) {
        val temp = playlists.value
        temp?.add(playlist)
        playlists.value = temp
    }

    fun updatePlaylist(playlist: com.nafanya.player.Playlist) {
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

    fun deletePlaylist(playlist: com.nafanya.player.Playlist) {
        val temp = playlists.value
        temp?.remove(playlist)
        playlists.value = temp
    }
}
