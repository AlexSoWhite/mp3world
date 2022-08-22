package com.nafanya.mp3world.core.entrypoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import com.nafanya.mp3world.features.albums.AlbumListManager
import com.nafanya.mp3world.features.allPlaylists.PlaylistListManager
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.allSongs.asAllSongsPlaylist
import com.nafanya.mp3world.features.artists.ArtistListManager
import com.nafanya.mp3world.features.favorites.FavouriteListManager
import com.nafanya.mp3world.features.localStorage.DatabaseHolder
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.launch

@Suppress("LongParameterList")
class InitialViewModel @Inject constructor(
    private val mediaStoreReader: MediaStoreReader,
    private val dbHolder: DatabaseHolder,
    private val playerInteractor: PlayerInteractor,
    private val songListManager: SongListManager,
    artistListManager: ArtistListManager,
    playlistListManager: PlaylistListManager,
    albumListManager: AlbumListManager,
    favouriteListManager: FavouriteListManager
) : ViewModel() {

    val songList = songListManager.songList
    val playlists = playlistListManager.playlists
    val artists = artistListManager.artists
    val albums = albumListManager.albums
    val favourites = favouriteListManager.favorites
    val initializationLiveData = playerInteractor.isPlayerInitialised

    fun initializeLists() {
        viewModelScope.launch {
            if (!isInitialized) {
                isInitialized = true
                // initialize songList
                mediaStoreReader.readMediaStore()
                songListManager.songList.observeForever {
                    if (playerInteractor.isPlayerInitialised.value != true) {
                        playerInteractor.setPlaylist(it.asAllSongsPlaylist())
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        dbHolder.closeDataBase()
    }

    companion object {
        private var isInitialized = false
    }
}
