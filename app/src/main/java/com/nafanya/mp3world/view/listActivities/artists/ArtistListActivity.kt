package com.nafanya.mp3world.view.listActivities.artists

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.listManagers.ArtistListManager
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListActivity
import com.nafanya.mp3world.viewmodel.PageState
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel

class ArtistListActivity : RecyclerHolderActivity() {
    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[SongListViewModel::class.java]
        viewModel.pageState.value = PageState.IS_LOADED
    }

    override fun setAdapter() {
        binding.recycler.adapter = ArtistListAdapter(ArtistListManager.artists) {
            val intent = Intent(this, SongListActivity::class.java)
            SongListActivity.newInstance(
                it.playlist?.songList!!,
                it.name!!
            )
            startActivity(intent)
        }
    }

    override fun getActivityDescription(): String {
        return "Исполнители (${ArtistListManager.artists.size})"
    }
}
