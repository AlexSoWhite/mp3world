package com.nafanya.mp3world.core.entrypoint

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import com.nafanya.mp3world.core.wrappers.local.LocalSong
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

    private val localInitializer = Observer<List<LocalSong>> { list ->
        viewModelScope.launch {
            playerInteractor.isPlayerInitialised.collect { isInitialized ->
                if (!isInitialized) {
                    playerInteractor.setPlaylist(list.asAllSongsPlaylist())
                }
            }
        }
    }

    fun initializeLists() {
        viewModelScope.launch {
            if (!isInitialized) {
                isInitialized = true
                // initialize songList
                mediaStoreReader.readMediaStore()
                songListManager.songList.observeForever(localInitializer)
            }
        }
    }

    /**
     * Called when [MainActivity] goes to destroyed state.
     * It's decided to keep player state when app is closed, so it can be restored if app opens
     * after a short period of time. So we simply pause the player when app is closed.
     */
    fun suspendPlayer() {
        playerInteractor.suspendPlayer()
    }

    override fun onCleared() {
        super.onCleared()
        songListManager.songList.removeObserver(localInitializer)
        dbHolder.closeDataBase()
    }

    companion object {
        private var isInitialized = false
    }
}
