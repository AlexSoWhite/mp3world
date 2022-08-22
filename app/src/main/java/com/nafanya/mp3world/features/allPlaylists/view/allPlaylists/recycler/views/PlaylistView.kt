package com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.utils.TextUtil
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.databinding.PlaylistListItemBinding
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.AllPlaylistsItemViewHolder
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.AllPlaylistsListItemView
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.ClickType

class PlaylistView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : AllPlaylistsListItemView(context, attributeSet, defStyle) {

    private val binding: PlaylistListItemBinding =
        PlaylistListItemBinding.inflate(LayoutInflater.from(context), this, true)

    fun setPlaylist(playlist: PlaylistWrapper, onClickCallback: (ClickType) -> Unit) {
        binding.playlistName.text = playlist.name
        binding.tracksCount.text = TextUtil.getCompositionsCountString(
            playlist.songList.size
        )
        if (playlist.image != null) {
            binding.playlistImage.setImageBitmap(playlist.image)
        } else {
            binding.playlistImage.setImageResource(R.drawable.playlist_play)
        }
        binding.playlistListItem.setOnClickListener {
            onClickCallback(ClickType.CLICK)
        }
        binding.playlistListItem.setOnLongClickListener {
            onClickCallback(ClickType.LONG)
            true
        }
    }
}

class PlaylistViewHolder(
    private val view: PlaylistView
) : AllPlaylistsItemViewHolder(view) {

    fun bind(
        playlist: PlaylistWrapper,
        onClickCallback: (ClickType) -> Unit
    ) {
        view.setPlaylist(playlist, onClickCallback)
    }
}
