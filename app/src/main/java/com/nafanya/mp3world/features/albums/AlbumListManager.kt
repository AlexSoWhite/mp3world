package com.nafanya.mp3world.features.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import com.nafanya.player.Playlist
import com.nafanya.player.Song
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Object that holds albums data. Populated by MediaStoreReader. Updates when MediaStoreReader is updated.
 */
@Singleton
class AlbumListManager @Inject constructor() : ListManager {

    private val mAlbums: MutableLiveData<List<Album>> = MutableLiveData(listOf())
    val albums: LiveData<List<Album>>
        get() = mAlbums

    private var suspendedList = mutableListOf<Album>()

    override suspend fun populate(mediaStoreReader: MediaStoreReader) {
        fillSuspendedList(mediaStoreReader.allSongs)
        updateData()
    }

    private fun fillSuspendedList(songList: List<Song>) {
        songList.forEach { song ->
            val album = Album(
                id = song.albumId,
                name = song.album,
                image = song.art
            )
            add(album, song)
        }
    }

    private fun add(element: Album, song: Song) {
        val index = suspendedList.indexOf(element)
        if (index != -1) {
            suspendedList
                .elementAt(index)
                .playlist
                ?.songList
                ?.add(song)
        } else {
            element.playlist = Playlist(
                arrayListOf(song),
                name = element.name
            )
            suspendedList.add(element)
        }
    }

    private fun updateData() {
        mAlbums.postValue(suspendedList)
        suspendedList = mutableListOf()
    }

    fun search(query: String): List<Album> {
        return mAlbums.value?.filter { it.name.lowercase().contains(query.lowercase()) } ?: listOf()
    }
}
