package com.nafanya.mp3world.view.legacy

import android.util.Log
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.view.listActivities.songs.SongListAdapter

@Deprecated(message = "Use SongListActivity instead")
class SongListFragment : RecyclerHolderFragment() {

    override fun setAdapter() {
        binding.recycler.adapter = SongListAdapter(SongListManager.getSongList()) {
            ForegroundServiceLiveDataProvider.currentPlaylist.value = Playlist(
                SongListManager.getSongList()
            )
        }
    }

    override fun getItemCount(): Int {
        return SongListManager.getSongList().size
    }

    override fun getFragmentDescription(): String {
        return "Мои песни"
    }

    override fun addCustomBehavior() {
        Log.d("detekt", "passed")
    }
}
