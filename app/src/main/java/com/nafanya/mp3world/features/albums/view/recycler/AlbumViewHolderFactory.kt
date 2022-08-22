package com.nafanya.mp3world.features.albums.view.recycler

import android.view.ViewGroup
import com.nafanya.mp3world.core.listUtils.recycler.BaseViewHolderFactory
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
