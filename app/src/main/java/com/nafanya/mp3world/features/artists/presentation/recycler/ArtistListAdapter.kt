package com.nafanya.mp3world.features.artists.presentation.recycler

import com.nafanya.mp3world.core.utils.list_utils.recycler.BaseAdapter
import com.nafanya.mp3world.core.utils.list_utils.recycler.BaseViewHolderFactory
import com.nafanya.mp3world.features.artists.domain.Artist

class ArtistListAdapter(
    private val callback: (Artist) -> Unit
) : BaseAdapter<ArtistListItem, ArtistViewHolder>(ArtistDiffUtilCallback) {

    override fun onBindViewHolder(
        holder: ArtistViewHolder,
        position: Int
    ) {
        val artist = getItem(position).getDataAsArtist()
        holder.bind(artist) { callback(artist) }
    }

    private val factory = ArtistViewHolderFactory()
    override val viewHolderFactory: BaseViewHolderFactory<ArtistViewHolder>
        get() = factory
}
