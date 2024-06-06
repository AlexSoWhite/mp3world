package com.nafanya.mp3world.features.artists.view.recycler

import com.nafanya.mp3world.core.utils.listUtils.recycler.BaseAdapter
import com.nafanya.mp3world.core.utils.listUtils.recycler.BaseViewHolderFactory
import com.nafanya.mp3world.features.artists.Artist

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
