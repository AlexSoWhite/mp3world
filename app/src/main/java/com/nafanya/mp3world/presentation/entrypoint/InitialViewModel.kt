package com.nafanya.mp3world.presentation.entrypoint

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.state_machines.StateModel
import com.nafanya.mp3world.domain.albums.AlbumPlaylistProvider
import com.nafanya.mp3world.domain.user_playlists.UserPlaylistsInteractor
import com.nafanya.mp3world.domain.all_songs.SongPlaylistProvider
import com.nafanya.mp3world.domain.all_songs.asAllSongsPlaylist
import com.nafanya.mp3world.domain.artists.ArtistPlaylistProvider
import com.nafanya.mp3world.domain.favourites.FavouritesProvider
import com.nafanya.mp3world.data.local_storage.LocalStorageRepository
import com.nafanya.mp3world.data.media_store.MediaStoreInteractor
import com.nafanya.player.interactor.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
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
    playlistListManager: UserPlaylistsInteractor,
    albumListManager: AlbumPlaylistProvider,
    favouriteListManager: FavouritesProvider
) : ViewModel() {

    private companion object {
        const val TAG = "_InitialViewModel"
    }

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
                    }.collectLatest {
                        success(it)
                    }
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            val songListDeferred = async { songListManager.songList.first() }
            mediaStoreInteractor.initializeSongList()

            combine(playerInteractor.isPlayerPresent, playerInteractor.isPlayerReady) { present, ready ->
                present && !ready
            }.filter { it }.collectLatest {
                Log.d(TAG, "player is present but not ready, submit initial playlist")
                playerInteractor.setPlaylist(songListDeferred.await().asAllSongsPlaylist())
            }
        }
    }

    // todo: check if we really have to do it manually
    override fun onCleared() {
        super.onCleared()
        localStorageRepository.closeDatabase()
    }
}
