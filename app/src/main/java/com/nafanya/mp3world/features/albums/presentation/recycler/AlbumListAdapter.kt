package com.nafanya.mp3world.features.albums.presentation.recycler

import com.nafanya.mp3world.core.utils.listUtils.recycler.BaseAdapter
import com.nafanya.mp3world.core.utils.listUtils.recycler.BaseViewHolderFactory
import com.nafanya.mp3world.features.albums.domain.Album

class AlbumListAdapter(
    private val onClick: (Album) -> Unit
) : BaseAdapter<AlbumListItem, AlbumViewHolder>(AlbumDiffUtilCallback) {

    override fun onBindViewHolder(
        holder: AlbumViewHolder,
        position: Int
    ) {
        val album = getItem(position).getDataAsAlbum()
        holder.bind(album) { onClick(album) }
    }

    private val factory = AlbumViewHolderFactory()
    override val viewHolderFactory: BaseViewHolderFactory<AlbumViewHolder>
        get() = factory
}
