package com.nafanya.mp3world.viewmodel.listViewModels.playlists

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState

class AddSongToListViewModel : ListViewModelInterface() {

    val pendingPlaylist: MutableLiveData<Playlist> by lazy {
        MutableLiveData<Playlist>()
    }

    val playlist: MutableLiveData<Playlist> by lazy {
        MutableLiveData<Playlist>()
    }

    override fun onLoading() {
        pendingPlaylist.value = passedPlaylist
        playlist.value = Playlist(
            SongListManager.songList.value!!,
            id = 0,
            name = passedPlaylist.name
        )
        if (playlist.value!!.songList.isEmpty()) {
            pageState.value = PageState.IS_EMPTY
        } else {
            pageState.value = PageState.IS_LOADED
        }
    }

    private fun triggerTitle() {
        title.value = "${pendingPlaylist.value!!.name} (${pendingPlaylist.value!!.songList.size})"
    }

    override fun onLoaded() {
        triggerTitle()
    }

    override fun onEmpty() {

    }

    fun addSong(song: Song) {
        pendingPlaylist.value?.songList?.add(song)
        triggerTitle()
    }

    fun deleteSong(song: Song) {
        pendingPlaylist.value?.songList?.remove(song)
        triggerTitle()
    }

    companion object {

        private lateinit var passedPlaylist: Playlist

        fun newInstance(playlist: Playlist) {
            this.passedPlaylist = playlist
        }
    }
}
