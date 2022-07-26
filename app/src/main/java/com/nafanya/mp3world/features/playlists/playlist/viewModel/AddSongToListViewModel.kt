package com.nafanya.mp3world.features.playlists.playlist.viewModel

import com.nafanya.player.Song
import com.nafanya.mp3world.features.allSongs.SongListViewModel
import com.nafanya.player.Playlist
import com.nafanya.mp3world.features.playlists.playlist.PlaylistViewModelProvider
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

class AddSongToListViewModel @Inject constructor(
    initializingPlaylist: Playlist,
    playerInteractor: PlayerInteractor
) : SongListViewModel(initializingPlaylist, playerInteractor) {

    private var passedPlaylist: Playlist
    private var playlistListViewModel = PlaylistViewModelProvider.takePlaylistViewModel()

    init {
        val tempSongList: MutableList<Song> = mutableListOf()
        playlistListViewModel?.playlist?.value?.songList?.forEach {
            tempSongList.add(it)
        }
        this.passedPlaylist = playlistListViewModel?.playlist?.value?.copy(
            songList = tempSongList
        )!!
    }

    fun isAdded(song: Song): Boolean {
        passedPlaylist.songList.forEach {
            if (it.id == song.id) return true
        }
        return false
    }

    private fun triggerTitle() {
        title.value = "${passedPlaylist.name} (${passedPlaylist.songList.size})"
    }

    override fun onLoaded() {
        triggerTitle()
    }

    override fun onEmpty() {
        title.value = passedPlaylist.name
    }

    fun addSong(song: Song) {
        passedPlaylist.songList.add(song)
        triggerTitle()
    }

    fun deleteSong(song: Song) {
        passedPlaylist.songList.remove(song)
        triggerTitle()
    }

    fun confirmChanges() {
        playlistListViewModel?.resetPlaylist(passedPlaylist)
    }
}
