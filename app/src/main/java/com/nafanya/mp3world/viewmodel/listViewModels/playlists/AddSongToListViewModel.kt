package com.nafanya.mp3world.viewmodel.listViewModels.playlists

import com.nafanya.mp3world.model.dependencies.PlaylistViewModelProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddSongToListViewModel @Inject constructor(
    initializingPlaylist: Playlist
) : SongListViewModel(initializingPlaylist) {

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
