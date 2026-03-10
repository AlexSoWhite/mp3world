package com.nafanya.mp3world.presentation.albums.recycler

import android.view.ViewGroup
import com.nafanya.mp3world.core.utils.list_utils.recycler.BaseViewHolderFactory
import com.nafanya.mp3world.core.utils.list_utils.recycler.view.BaseViewHolder
import com.nafanya.mp3world.domain.albums.Album
import java.lang.IllegalArgumentException

class AlbumViewHolderFactory : BaseViewHolderFactory<AlbumViewHolder> {

    override fun create(
        viewType: Int,
        parent: ViewGroup
    ): AlbumViewHolder = with(parent) {
        return when (viewType) {
            ALBUM -> {
                AlbumViewHolder(AlbumView(context))
            }
            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }
    }
}

class AlbumViewHolder(
    private val view: AlbumView
) : BaseViewHolder(view) {

    fun bind(album: Album, onClick: () -> Unit) {
        view.setAlbum(album, onClick)
    }
}
