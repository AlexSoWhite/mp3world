package com.nafanya.mp3world.features.playlists.playlistsList.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.utils.TextUtil
import com.nafanya.mp3world.databinding.PlaylistListItemBinding
import com.nafanya.player.Playlist

enum class ClickType {
    CLICK,
    LONG
}

class PlaylistListAdapter(
    private val playlistList: List<com.nafanya.player.Playlist>,
    private val callback: (com.nafanya.player.Playlist, ClickType) -> Unit
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
        private val callback: (com.nafanya.player.Playlist, ClickType) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = PlaylistListItemBinding.bind(itemView)

        fun bind(playlist: com.nafanya.player.Playlist) {
            binding.playlist = playlist
            binding.tracksCount = TextUtil.getCompositionsCountString(
                playlist.songList.size
            )
            if (playlist.image != null) {
                binding.playlistImage.setImageBitmap(playlist.image)
            } else {
                binding.playlistImage.setImageResource(R.drawable.playlist_play)
            }
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
