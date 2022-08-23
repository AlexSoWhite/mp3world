package com.nafanya.mp3world.features.playerView.view.currentPlaylist

import androidx.lifecycle.map
import com.nafanya.mp3world.core.viewModel.StatePlaylistViewModel
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong
import com.nafanya.mp3world.features.songListViews.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.songListViews.SONG_REMOTE
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

class CurrentPlaylistViewModel @Inject constructor(
    playerInteractor: PlayerInteractor,
) : StatePlaylistViewModel(
    playerInteractor,
    playerInteractor.currentPlaylist.map { it as PlaylistWrapper }
) {

    override fun List<SongWrapper>.asListItems(): List<SongListItem> {
        val list = mutableListOf<SongListItem>()
        this.forEach {
            when (it) {
                is LocalSong -> list.add(SongListItem(SONG_LOCAL_IMMUTABLE, it))
                is RemoteSong -> list.add(SongListItem(SONG_REMOTE, it))
            }
        }
        return list
    }
}
