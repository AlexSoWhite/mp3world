package com.nafanya.mp3world.features.entrypoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.stateMachines.StateModel
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.features.albums.AlbumListManager
import com.nafanya.mp3world.features.allPlaylists.PlaylistListManagerImpl
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.allSongs.asAllSongsPlaylist
import com.nafanya.mp3world.features.artists.ArtistListManagerImpl
import com.nafanya.mp3world.features.favourites.FavouritesManager
import com.nafanya.mp3world.features.localStorage.LocalStorageInteractor
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * TODO: remove initialization logic
 */
@Suppress("LongParameterList")
class InitialViewModel @Inject constructor(
    private val mediaStoreInteractor: MediaStoreInteractor,
    private val localStorageInteractor: LocalStorageInteractor,
    private val playerInteractor: PlayerInteractor,
    private val songListManager: SongListManager,
    artistListManager: ArtistListManagerImpl,
    playlistListManager: PlaylistListManagerImpl,
    albumListManager: AlbumListManager,
    favouriteListManager: FavouritesManager
) : ViewModel() {

    val songModel = songListManager.songList.asIntModel()
    val playlists = playlistListManager.playlists.asIntModel()
    val artists = artistListManager.artists.asIntModel()
    val albums = albumListManager.albums.asIntModel()
    val favourites = favouriteListManager.favorites.map { it.songList }.asIntModel()

    private inline fun <reified T : List<Any>> Flow<T>.asIntModel(): StateModel<Int> {
        return StateModel<Int>().apply {
            this.load {
                viewModelScope.launch {
                    this@asIntModel.map {
                        it.size
                    }.collect {
                        success(it)
                    }
                }
            }
        }
    }

    private val localInitializer = FlowCollector<List<LocalSong>> { list ->
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
                mediaStoreInteractor.readMediaStore()
                // TODO flow
                songListManager.songList.collect(localInitializer)
            }
        }
    }

    /**
     * Called when [MainActivity] goes to destroyed state.
     * It's decided to keep player state when app is closed, so it can be restored if app opens
     * after a short period of time. So we simply pause the player when app is closed.
     *
     * TODO fix it as system shows annoying notification
     */
    fun suspendPlayer() {
        playerInteractor.suspendPlayer()
    }

    override fun onCleared() {
        super.onCleared()
        localStorageInteractor.closeDataBase()
    }

    companion object {
        private var isInitialized = false
    }
}
