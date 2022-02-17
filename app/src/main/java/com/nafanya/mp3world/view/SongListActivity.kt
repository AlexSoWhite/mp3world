package com.nafanya.mp3world.view

import com.nafanya.mp3world.model.Playlist
import com.nafanya.mp3world.model.Song
import com.nafanya.mp3world.viewmodel.ForegroundServiceLiveDataProvider

class SongListActivity : RecyclerHolderActivity() {

    override fun setAdapter() {
        binding.recycler.adapter = SongListAdapter(songList) {
            ForegroundServiceLiveDataProvider.currentPlaylist.value = Playlist(songList)
        }
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    override fun getFragmentDescription(): String {
        return description
    }

    companion object {

        private lateinit var songList: ArrayList<Song>
        private lateinit var description: String

        fun newInstance(
            songList: ArrayList<Song>,
            description: String
        ) {
            this.songList = songList
            this.description = description
        }
    }
}
