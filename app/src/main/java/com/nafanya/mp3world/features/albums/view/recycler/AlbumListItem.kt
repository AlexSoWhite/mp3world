package com.nafanya.mp3world.features.albums.view.recycler

import androidx.recyclerview.widget.DiffUtil
import com.nafanya.mp3world.core.utils.listUtils.recycler.BaseListItem
import com.nafanya.mp3world.features.albums.Album

const val ALBUM = 0

data class AlbumListItem(
    override val itemType: Int,
    override val data: Any
) : BaseListItem() {

    fun getDataAsAlbum(): Album {
        return data as Album
    }
}

val AlbumDiffUtilCallback = object : DiffUtil.ItemCallback<AlbumListItem>() {
    override fun areItemsTheSame(oldItem: AlbumListItem, newItem: AlbumListItem): Boolean {
        if (oldItem.itemType != newItem.itemType) return false
        return when (oldItem.itemType) {
            ALBUM -> oldItem.getDataAsAlbum().id == newItem.getDataAsAlbum().id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: AlbumListItem, newItem: AlbumListItem): Boolean {
        if (oldItem.itemType == ALBUM) {
            val oldData = oldItem.getDataAsAlbum()
            val newData = newItem.getDataAsAlbum()
            return oldData.name == newData.name &&
                oldData.playlist == newData.playlist &&
                oldData.imageSource?.equals(newData.imageSource) == true
        }
        return false
    }
}
