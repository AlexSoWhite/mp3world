package com.nafanya.mp3world.features.allPlaylists.allPlaylists.recycler.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.utils.TextUtil
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.databinding.PlaylistContainerViewBinding
import com.nafanya.mp3world.features.allPlaylists.allPlaylists.recycler.AllPlaylistsItemViewHolder
import com.nafanya.mp3world.features.allPlaylists.allPlaylists.recycler.AllPlaylistsListItemView
import com.nafanya.mp3world.features.allPlaylists.allPlaylists.recycler.ClickType

class PlaylistView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : AllPlaylistsListItemView(context, attributeSet, defStyle) {

    private val binding: PlaylistContainerViewBinding =
        PlaylistContainerViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    fun setPlaylist(
        playlist: PlaylistWrapper,
        onClickCallback: (ClickType) -> Unit
    ) = binding.apply {
        itemTitle.text = playlist.name
        tracksCount.text = TextUtil.getCompositionsCountString(
            playlist.songList.size
        )
        Glide.with(context)
            .load(playlist.imageSource)
            .placeholder(R.drawable.playlist_play)
            .into(itemImage)
        setOnClickListener {
            onClickCallback(ClickType.CLICK)
        }
        setOnLongClickListener {
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
