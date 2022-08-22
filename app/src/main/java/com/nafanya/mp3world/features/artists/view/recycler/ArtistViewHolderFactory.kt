package com.nafanya.mp3world.features.artists.view.recycler

import android.view.ViewGroup
import com.nafanya.mp3world.core.listUtils.recycler.BaseViewHolderFactory

class ArtistViewHolderFactory : BaseViewHolderFactory<ArtistViewHolder> {

    override fun create(
        viewType: Int,
        parent: ViewGroup
    ): ArtistViewHolder = with(parent) {
        return when (viewType) {
            ARTIST -> {
                ArtistViewHolder(ArtistView(parent.context))
            }
            else -> ArtistViewHolder(ArtistView(context))
        }
    }
}
