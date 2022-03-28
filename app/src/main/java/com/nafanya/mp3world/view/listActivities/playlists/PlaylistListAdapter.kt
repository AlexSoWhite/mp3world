package com.nafanya.mp3world.view.listActivities.playlists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.PlaylistListItemBinding
import com.nafanya.mp3world.model.wrappers.Playlist

enum class ClickType {
    CLICK,
    LONG
}

class PlaylistListAdapter(
    private val playlistList: List<Playlist>,
    private val callback: (Playlist, ClickType) -> Unit
) : RecyclerView.Adapter<PlaylistListAdapter.PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val itemView = LayoutInflater.from(
            parent.context
        ).inflate(R.layout.playlist_list_item, parent, false)
        return PlaylistViewHolder(itemView, callback)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlistList[position])
    }

    override fun getItemCount(): Int {
        return playlistList.size
    }

    class PlaylistViewHolder(
        itemView: View,
        private val callback: (Playlist, ClickType) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = PlaylistListItemBinding.bind(itemView)

        fun bind(playlist: Playlist) {
            binding.playlist = playlist
            binding.tracksCount = playlist.songList.size.toString()
            binding.playlistListItem.setOnClickListener {
                callback(playlist, ClickType.CLICK)
            }
            binding.playlistListItem.setOnLongClickListener {
                callback(playlist, ClickType.LONG)
                true
            }
        }
    }
}
