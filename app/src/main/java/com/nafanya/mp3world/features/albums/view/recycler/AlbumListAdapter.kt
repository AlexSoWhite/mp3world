package com.nafanya.mp3world.features.albums.view.recycler

import com.nafanya.mp3world.core.listUtils.recycler.BaseAdapter
import com.nafanya.mp3world.core.listUtils.recycler.BaseViewHolderFactory
import com.nafanya.mp3world.features.albums.Album

class AlbumListAdapter(
    private val callback: (Album) -> Unit
) : BaseAdapter<AlbumListItem, AlbumViewHolder>(AlbumDiffUtilCallback) {

    override fun onBindViewHolder(
        holder: AlbumViewHolder,
        position: Int
    ) {
        val album = getItem(position).getDataAsAlbum()
        holder.bind(album) { callback(album) }
    }

    private val factory = AlbumViewHolderFactory()
    override val viewHolderFactory: BaseViewHolderFactory<AlbumViewHolder>
        get() = factory
}
