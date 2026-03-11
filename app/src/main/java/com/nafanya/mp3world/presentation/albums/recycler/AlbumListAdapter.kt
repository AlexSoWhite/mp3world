package com.nafanya.mp3world.presentation.albums.recycler

import com.nafanya.mp3world.core.utils.list_utils.recycler.BaseAdapter
import com.nafanya.mp3world.core.utils.list_utils.recycler.BaseViewHolderFactory
import com.nafanya.mp3world.domain.albums.Album

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
