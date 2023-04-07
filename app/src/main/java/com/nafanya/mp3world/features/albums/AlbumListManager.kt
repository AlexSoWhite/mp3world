package com.nafanya.mp3world.features.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.mediaStore.MediaStoreInteractor
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Object that holds albums data. Populated by [MediaStoreInteractor]. Updates when [MediaStoreInteractor] is updated.
 */
@Singleton
class AlbumListManager @Inject constructor(
    mediaStoreInteractor: MediaStoreInteractor,
    ioCoroutineProvider: IOCoroutineProvider
) : ListManager() {

    private val mAlbums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>>
        get() = mAlbums

    private var suspendedList = mutableListOf<Album>()

    init {
        ioCoroutineProvider.ioScope.launch {
            mediaStoreInteractor.allSongs.collectLatest {
                it?.let { list ->
                    fillSuspendedList(list)
                    updateData()
                }
            }
        }
    }

    override fun getPlaylistByContainerId(id: Long): LiveData<PlaylistWrapper?> {
        return albums.map {
            it.firstOrNull { album ->
                album.id == id
            }?.playlist
        }
    }

    private fun fillSuspendedList(songList: List<LocalSong>) {
        songList.forEach { song ->
            val album = Album(
                id = song.albumId,
                name = song.album,
                image = song.art
            )
            add(album, song)
        }
    }

    private fun add(element: Album, song: LocalSong) {
        val index = suspendedList.indexOf(element)
        if (index != -1) {
            val playlist = suspendedList[index].playlist
            suspendedList.elementAt(index).playlist = playlist?.copy(
                songList = listOf(song) + playlist.songList
            )
        } else {
            element.playlist = PlaylistWrapper(
                listOf(song),
                name = element.name
            )
            suspendedList.add(element)
        }
    }

    private fun updateData() {
        mAlbums.postValue(suspendedList)
        suspendedList = mutableListOf()
    }
}
