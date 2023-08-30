package com.nafanya.mp3world.features.artists.view.recycler

import androidx.recyclerview.widget.DiffUtil
import com.nafanya.mp3world.core.listUtils.recycler.BaseListItem
import com.nafanya.mp3world.features.artists.Artist

const val ARTIST = 0

data class ArtistListItem(
    override val itemType: Int,
    override val data: Any
) : BaseListItem() {

    fun getDataAsArtist(): Artist {
        return data as Artist
    }
}

val ArtistDiffUtilCallback = object : DiffUtil.ItemCallback<ArtistListItem>() {
    override fun areItemsTheSame(oldItem: ArtistListItem, newItem: ArtistListItem): Boolean {
        if (oldItem.itemType != newItem.itemType) return false
        return when (oldItem.itemType) {
            ARTIST -> oldItem.getDataAsArtist().id == newItem.getDataAsArtist().id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: ArtistListItem, newItem: ArtistListItem): Boolean {
        if (oldItem.itemType == ARTIST) {
            val oldData = oldItem.getDataAsArtist()
            val newData = newItem.getDataAsArtist()
            return oldData.name == newData.name &&
                oldData.playlist == newData.playlist &&
                oldData.imageSource?.equals(newData.imageSource) == true
        }
        return false
    }
}
