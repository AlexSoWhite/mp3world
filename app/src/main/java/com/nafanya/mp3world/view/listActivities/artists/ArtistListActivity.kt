package com.nafanya.mp3world.view.listActivities.artists

import android.content.Intent
import com.nafanya.mp3world.model.listManagers.ArtistListManager
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListActivity

class ArtistListActivity : RecyclerHolderActivity() {

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

    override fun getItemCount(): Int {
        return ArtistListManager.artists.size
    }

    override fun getFragmentDescription(): String {
        return "Исполнители"
    }
}
