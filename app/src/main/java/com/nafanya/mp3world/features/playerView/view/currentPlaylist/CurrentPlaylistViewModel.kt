package com.nafanya.mp3world.features.playerView.view.currentPlaylist

import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import com.nafanya.mp3world.core.mediaStore.MediaStoreInteractor
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong
import com.nafanya.mp3world.features.downloading.DownloadInteractor
import com.nafanya.mp3world.features.downloading.DownloadingViewModel
import com.nafanya.mp3world.features.songListViews.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.songListViews.SONG_REMOTE
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

class CurrentPlaylistViewModel @Inject constructor(
    playerInteractor: PlayerInteractor,
    override val downloadInteractor: DownloadInteractor,
    override val mediaStoreInteractor: MediaStoreInteractor
) : StatedPlaylistViewModel(),
    DownloadingViewModel {

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
}
