package com.nafanya.mp3world.presentation.player_view.current_playlist

import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.data.downloading.api.DownloadInteractor
import com.nafanya.mp3world.data.downloading.api.DownloadingViewModel
import com.nafanya.mp3world.presentation.song_list_views.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.presentation.song_list_views.SONG_REMOTE
import com.nafanya.mp3world.presentation.song_list_views.SongListItem
import com.nafanya.player.interactor.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.flow.map

class CurrentPlaylistViewModel @Inject constructor(
    private val downloadInteractor: DownloadInteractor,
    override val playerInteractor: PlayerInteractor
) : StatedPlaylistViewModel(),
    DownloadingViewModel {

    override val playlistFlow = playerInteractor.currentPlaylist.map {
        it as PlaylistWrapper
    }

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

    override fun download(remoteSong: RemoteSong) = downloadInteractor.download(remoteSong)
}
