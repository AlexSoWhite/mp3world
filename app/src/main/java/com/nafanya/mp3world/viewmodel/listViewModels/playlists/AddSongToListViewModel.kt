package com.nafanya.mp3world.viewmodel.listViewModels.playlists

import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.dependencies.PlaylistListViewModelProvider
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

    init {
        val tempSongList: MutableList<Song> = mutableListOf()
        initializingPlaylist.songList.forEach {
            tempSongList.add(it)
        }
        this.passedPlaylist = initializingPlaylist.copy(
            songList = tempSongList
        )
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
        PlaylistViewModelProvider.takePlaylistViewModel()?.resetPlaylist(passedPlaylist)
        PlaylistListViewModelProvider.takePlaylistListViewModel()?.updatePlaylist(passedPlaylist)
    }
}
