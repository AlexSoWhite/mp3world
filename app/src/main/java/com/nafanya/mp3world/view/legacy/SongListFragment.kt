package com.nafanya.mp3world.view.legacy

import android.util.Log
import com.nafanya.mp3world.model.Playlist
import com.nafanya.mp3world.model.SongListManager
import com.nafanya.mp3world.view.SongListAdapter
import com.nafanya.mp3world.viewmodel.ForegroundServiceLiveDataProvider

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
