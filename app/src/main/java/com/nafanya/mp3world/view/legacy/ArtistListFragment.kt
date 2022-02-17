package com.nafanya.mp3world.view.legacy

import android.view.View
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.listManagers.ArtistListManager
import com.nafanya.mp3world.view.listActivities.artists.ArtistListAdapter
import com.nafanya.mp3world.view.listActivities.songs.SongListAdapter

@Deprecated(message = "Use ArtistListActivity instead")
class ArtistListFragment : RecyclerHolderFragment() {

    private var isOnArtistPlaylist = false

    override fun setAdapter() {
        binding.recycler.adapter = ArtistListAdapter(ArtistListManager.artists) { artist ->
            isOnArtistPlaylist = true
            binding.goBack.visibility = View.VISIBLE
            binding.recycler.adapter = artist.playlist?.songList?.let { it ->
                SongListAdapter(it) {
                    ForegroundServiceLiveDataProvider.currentPlaylist.value = artist.playlist
                }
            }
            binding.fragmentDescription = artist.name
            binding.itemCount = artist.playlist?.songList?.size
        }
    }

    override fun addCustomBehavior() {
        binding.goBack.setOnClickListener {
            if (isOnArtistPlaylist) {
                binding.goBack.visibility = View.INVISIBLE
                binding.fragmentDescription = "Исполнители"
                binding.itemCount = ArtistListManager.artists.size
                setAdapter()
                isOnArtistPlaylist = false
            }
        }
    }

    override fun getItemCount(): Int {
        return ArtistListManager.artists.size
    }

    override fun getFragmentDescription(): String {
        return "Исполнители"
    }
}
