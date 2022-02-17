package com.nafanya.mp3world.view.listActivities.songs

import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity

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
            Companion.songList = songList
            Companion.description = description
        }
    }
}
