package com.nafanya.mp3world.features.entrypoint

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.stateMachines.StateModel
import com.nafanya.mp3world.core.wrappers.LocalSong
import com.nafanya.mp3world.features.albums.AlbumListManager
import com.nafanya.mp3world.features.allPlaylists.PlaylistListManager
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.allSongs.asAllSongsPlaylist
import com.nafanya.mp3world.features.artists.ArtistListManager
import com.nafanya.mp3world.features.favorites.FavouriteListManager
import com.nafanya.mp3world.features.localStorage.DatabaseHolder
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * TODO: remove initialization logic
 */
@Suppress("LongParameterList")
class InitialViewModel @Inject constructor(
    private val mediaStoreInteractor: MediaStoreInteractor,
    private val dbHolder: DatabaseHolder,
    private val playerInteractor: PlayerInteractor,
    private val songListManager: SongListManager,
    artistListManager: ArtistListManager,
    playlistListManager: PlaylistListManager,
    albumListManager: AlbumListManager,
    favouriteListManager: FavouriteListManager
) : ViewModel() {

    val songModel = songListManager.songList.asIntModel()
    val playlists = playlistListManager.playlists.asIntModel()
    val artists = artistListManager.artists.asIntModel()
    val albums = albumListManager.albums.asIntModel()
    val favourites = favouriteListManager.favorites.map { it.songList }.asIntModel()

    private inline fun <reified T : List<Any>> LiveData<T>.asIntModel(): StateModel<Int> {
        return StateModel<Int>().apply {
            this.load {
                viewModelScope.launch {
                    this@asIntModel.asFlow().map {
                        it.size
                    }.collect {
                        success(it)
                    }
                }
            }
        }
    }

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
                mediaStoreInteractor.readMediaStore()
                songListManager.songList.observeForever(localInitializer)
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
        songListManager.songList.removeObserver(localInitializer)
        dbHolder.closeDataBase()
    }

    companion object {
        private var isInitialized = false
    }
}
