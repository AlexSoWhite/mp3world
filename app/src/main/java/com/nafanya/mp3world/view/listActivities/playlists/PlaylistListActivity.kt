package com.nafanya.mp3world.view.listActivities.playlists

import android.content.Intent
import android.view.View
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListActivity

class PlaylistListActivity : RecyclerHolderActivity() {

    override fun setAdapter() {
        binding.recycler.adapter = PlaylistListAdapter(
            PlaylistListManager.playlists
        ) {
            val intent = Intent(this, SongListActivity::class.java)
            SongListActivity.newInstance(
                it.songList,
                it.name
            )
            startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun getFragmentDescription(): String {
        return "Мои плейлисты"
    }

    override fun addCustomBehavior() {
        super.addCustomBehavior()
        binding.addPlaylist.addPlaylist.visibility = View.VISIBLE
        binding.addPlaylist.addPlaylist.setOnClickListener {
            val intent = Intent(this, AddPlaylistDialogActivity::class.java)
            startActivity(intent)
        }
    }
}
