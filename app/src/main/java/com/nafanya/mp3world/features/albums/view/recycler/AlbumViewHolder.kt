package com.nafanya.mp3world.features.albums.view.recycler

import com.nafanya.mp3world.core.listUtils.recycler.views.BaseViewHolder
import com.nafanya.mp3world.features.albums.Album

class AlbumViewHolder(
    private val view: AlbumView
) : BaseViewHolder(view) {

    fun bind(album: Album, onClick: () -> Unit) {
        view.setAlbum(album, onClick)
    }
}
