package com.nafanya.mp3world.view

import android.util.Log
import com.nafanya.mp3world.model.SongListManager

class SongListFragment : RecyclerHolderFragment() {

    override fun setAdapter() {
        binding.recycler.adapter = SongListAdapter(SongListManager.getSongList()) {}
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
