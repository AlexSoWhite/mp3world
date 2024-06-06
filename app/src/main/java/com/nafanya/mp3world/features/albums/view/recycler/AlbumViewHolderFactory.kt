package com.nafanya.mp3world.features.albums.view.recycler

import android.view.ViewGroup
import com.nafanya.mp3world.core.utils.listUtils.recycler.BaseViewHolderFactory
import com.nafanya.mp3world.core.utils.listUtils.recycler.commonUi.BaseViewHolder
import com.nafanya.mp3world.features.albums.Album
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
