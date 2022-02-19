package com.nafanya.mp3world.view.listActivities.songs

import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.viewmodel.PageState
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel

class SongListActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[SongListViewModel::class.java]
        if (query != null) {
            (viewModel as SongListViewModel).startLoading(query!!) {
                songList = it.songList
                runOnUiThread {
                    this.supportActionBar?.title = "$description (${songList.size})"
                }
            }
        } else {
            viewModel.pageState.value = PageState.IS_LOADED
        }
    }

    override fun setAdapter() {
        binding.recycler.adapter = SongListAdapter(songList) {
            ForegroundServiceLiveDataProvider.currentPlaylist.value = Playlist(songList)
        }
    }

    override fun getActivityDescription(): String {
        return if (query == null) {
            "$description (${songList.size})"
        } else {
            description
        }
    }

    companion object {

        private lateinit var songList: ArrayList<Song>
        private lateinit var description: String
        private var query: String? = null

        fun newInstance(
            songList: ArrayList<Song>,
            description: String,
            query: String? = null
        ) {
            this.songList = songList
            this.description = description
            this.query = query
        }
    }
}
