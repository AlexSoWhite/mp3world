package com.nafanya.mp3world.view

import android.view.View
import com.nafanya.mp3world.model.ArtistListManager
import com.nafanya.mp3world.viewmodel.ForegroundServiceLiveDataProvider

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
