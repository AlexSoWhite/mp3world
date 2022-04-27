package com.nafanya.mp3world.viewmodel.listViewModels.playlists

import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddSongToListViewModel @Inject constructor(
    initializingPlaylist: Playlist
) : SongListViewModel(initializingPlaylist) {

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
        passedViewModel.resetPlaylist(passedPlaylist)
    }

    companion object {

        private lateinit var passedViewModel: PlaylistViewModel
        private lateinit var passedPlaylist: Playlist

        fun newInstance(passedViewModel: PlaylistViewModel) {
            this.passedViewModel = passedViewModel
            val tempSongList: MutableList<Song> = mutableListOf()
            passedViewModel.playlist.value!!.songList.forEach {
                tempSongList.add(it)
            }
            this.passedPlaylist = passedViewModel.playlist.value!!.copy(
                songList = tempSongList
            )
        }
    }
}
