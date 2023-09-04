package com.nafanya.mp3world.features.playerView.view.currentPlaylist

import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.stateMachines.commonUi.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.features.downloading.DownloadInteractor
import com.nafanya.mp3world.features.downloading.DownloadingViewModel
import com.nafanya.mp3world.features.favourites.FavouritesManager
import com.nafanya.mp3world.features.favourites.FavouritesManagerProxy
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import com.nafanya.mp3world.features.songListViews.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.songListViews.SONG_REMOTE
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.launch

class CurrentPlaylistViewModel @Inject constructor(
    override val downloadInteractor: DownloadInteractor,
    override val mediaStoreInteractor: MediaStoreInteractor,
    private val favouritesManager: FavouritesManager,
    playerInteractor: PlayerInteractor
) : StatedPlaylistViewModel(),
    DownloadingViewModel,
    FavouritesManagerProxy {

    override val playlistFlow = playerInteractor.currentPlaylist.map {
        it as PlaylistWrapper
    }.asFlow()

    override fun asListItems(list: List<SongWrapper>): List<SongListItem> {
        val newList = mutableListOf<SongListItem>()
        list.forEach {
            when (it) {
                is LocalSong -> newList.add(SongListItem(SONG_LOCAL_IMMUTABLE, it))
                is RemoteSong -> newList.add(SongListItem(SONG_REMOTE, it))
            }
        }
        return newList
    }

    override fun isSongInFavourites(song: LocalSong) = favouritesManager.isSongInFavourites(song)

    override fun addFavourite(song: LocalSong) {
        viewModelScope.launch {
            favouritesManager.add(song)
        }
    }

    override fun deleteFavourite(song: LocalSong) {
        viewModelScope.launch {
            favouritesManager.delete(song)
        }
    }
}
