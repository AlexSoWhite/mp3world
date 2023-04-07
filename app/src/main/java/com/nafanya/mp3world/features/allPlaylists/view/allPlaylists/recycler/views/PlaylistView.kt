package com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.utils.TextUtil
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.databinding.PlaylistContainerViewBinding
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.AllPlaylistsItemViewHolder
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.AllPlaylistsListItemView
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.ClickType

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
        if (playlist.image != null) {
            itemImage.setImageBitmap(playlist.image)
        } else {
            itemImage.setImageResource(R.drawable.playlist_play)
        }
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
