package com.nafanya.mp3world.presentation.entrypoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.state_machines.StateModel
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.domain.albums.AlbumPlaylistProvider
import com.nafanya.mp3world.domain.user_playlists.UserPlaylistInteractor
import com.nafanya.mp3world.domain.all_songs.SongPlaylistProvider
import com.nafanya.mp3world.domain.all_songs.asAllSongsPlaylist
import com.nafanya.mp3world.domain.artists.ArtistPlaylistProvider
import com.nafanya.mp3world.domain.favourites.FavouritesProvider
import com.nafanya.mp3world.data.local_storage.LocalStorageRepository
import com.nafanya.mp3world.data.media_store.MediaStoreInteractor
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
    private val localStorageRepository: LocalStorageRepository, // todo: maybe should be a domain level model
    private val playerInteractor: PlayerInteractor,
    private val songListManager: SongPlaylistProvider,
    artistListManager: ArtistPlaylistProvider,
    playlistListManager: UserPlaylistInteractor,
    albumListManager: AlbumPlaylistProvider,
    favouriteListManager: FavouritesProvider
) : ViewModel() {

    val songModel = songListManager.songList.loadIntModel()
    val playlists = playlistListManager.playlists.loadIntModel()
    val artists = artistListManager.artists.loadIntModel()
    val albums = albumListManager.albums.loadIntModel()
    val favourites = favouriteListManager.favorites.map { it.songList }.loadIntModel()

    private inline fun <reified T : List<Any>> Flow<T>.loadIntModel(): StateModel<Int> {
        return StateModel<Int>().apply {
            this.load {
                viewModelScope.launch {
                    this@loadIntModel.map {
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
                mediaStoreInteractor.initializeSongList()
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
        localStorageRepository.closeDatabase()
    }

    companion object {
        private var isInitialized = false
    }
}
