package com.nafanya.mp3world.features.artists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Object that holds artists data. Populated by MediaStoreReader.
 */
@Singleton
class ArtistListManager @Inject constructor(
    mediaStoreReader: MediaStoreReader
) : ListManager() {

    private val mArtists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>>
        get() = mArtists

    private var suspendedList = mutableListOf<Artist>()

    init {
        mediaStoreReader.allSongs.observeForever {
            fillSuspendList(it)
            updateData()
        }
    }

    override fun getPlaylistByContainerId(id: Long): LiveData<PlaylistWrapper?> {
        return artists.map {
            it.firstOrNull { artist ->
                artist.id == id
            }?.playlist
        }
    }

    private fun fillSuspendList(songList: List<LocalSong>) {
        songList.forEach { song ->
            val artist = Artist(
                name = song.artist,
                id = song.artistId,
                image = song.art
            )
            add(artist, song)
        }
    }

    private fun add(element: Artist, song: LocalSong) {
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
        mArtists.postValue(suspendedList)
        suspendedList = mutableListOf()
    }
}
