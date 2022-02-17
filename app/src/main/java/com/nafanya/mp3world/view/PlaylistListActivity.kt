package com.nafanya.mp3world.view

import com.nafanya.mp3world.model.Song

class PlaylistListActivity : RecyclerHolderActivity() {

    override fun setAdapter() {
        binding.recycler.adapter = SongListAdapter(arrayListOf<Song>()) {}
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun getFragmentDescription(): String {
        return "Мои плейлисты"
    }
}
