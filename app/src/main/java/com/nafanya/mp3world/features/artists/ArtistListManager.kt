package com.nafanya.mp3world.features.artists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import com.nafanya.player.Playlist
import com.nafanya.player.Song
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Object that holds artists data. Populated by MediaStoreReader.
 */
@Singleton
class ArtistListManager @Inject constructor() : ListManager {

    private val mArtists: MutableLiveData<List<Artist>> = MutableLiveData(listOf())
    val artists: LiveData<List<Artist>>
        get() = mArtists

    private var suspendedList = mutableListOf<Artist>()

    override suspend fun populate(mediaStoreReader: MediaStoreReader) {
        fillSuspendList(mediaStoreReader.allSongs)
        updateData()
    }

    private fun fillSuspendList(songList: List<Song>) {
        songList.forEach { song ->
            val artist = Artist(
                name = song.artist,
                id = song.artistId,
                image = song.art
            )
            add(artist, song)
        }
    }

    private fun add(element: Artist, song: Song) {
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
        mArtists.postValue(suspendedList)
        suspendedList = mutableListOf()
    }

    fun search(query: String): List<Artist> {
        return mArtists.value?.filter {
            it.name.lowercase().contains(query.lowercase())
        } ?: listOf()
    }
}
