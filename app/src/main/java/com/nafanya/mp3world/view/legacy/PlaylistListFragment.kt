package com.nafanya.mp3world.view.legacy

import android.util.Log
import com.nafanya.mp3world.model.SongListManager
import com.nafanya.mp3world.view.SongListAdapter

@Deprecated(message = "Use PlaylistListActivity instead")
class PlaylistListFragment : RecyclerHolderFragment() {

    override fun setAdapter() {
        binding.recycler.adapter = SongListAdapter(SongListManager.getSongList()) {}
    }

    override fun getItemCount(): Int {
        return SongListManager.getSongList().size
    }

    override fun getFragmentDescription(): String {
        return "Мои плейлисты"
    }

    override fun addCustomBehavior() {
        Log.d("detekt", "passed")
    }
}
