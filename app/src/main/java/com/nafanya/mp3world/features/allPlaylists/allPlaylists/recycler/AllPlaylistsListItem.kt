package com.nafanya.mp3world.features.allPlaylists.allPlaylists.recycler

import androidx.annotation.IntDef
import androidx.recyclerview.widget.DiffUtil
import com.nafanya.mp3world.core.utils.listUtils.recycler.BaseListItem
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    PLAYLIST,
    ADD_PLAYLIST_BUTTON
)
annotation class PlaylistListItemViewType
const val PLAYLIST = 0
const val ADD_PLAYLIST_BUTTON = 1

data class AllPlaylistsListItem(
    override val itemType: Int,
    override val data: Any
) : BaseListItem() {

    fun getDataAsPlaylist(): PlaylistWrapper {
        return data as PlaylistWrapper
    }
}

val PlaylistDiffUtilCallback = object : DiffUtil.ItemCallback<AllPlaylistsListItem>() {

    override fun areItemsTheSame(
        oldItem: AllPlaylistsListItem,
        newItem: AllPlaylistsListItem
    ): Boolean {
        return when {
            oldItem.itemType != newItem.itemType -> false
            oldItem.itemType == PLAYLIST -> {
                oldItem.getDataAsPlaylist() == newItem.getDataAsPlaylist()
            }
            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: AllPlaylistsListItem,
        newItem: AllPlaylistsListItem
    ): Boolean {
        return if (oldItem.itemType == PLAYLIST) {
            val oldPlaylist = oldItem.getDataAsPlaylist()
            val newPlaylist = newItem.getDataAsPlaylist()
            (oldPlaylist.id == newPlaylist.id) &&
                (oldPlaylist.name == newPlaylist.name) &&
                (oldPlaylist.imageSource?.equals(newPlaylist.imageSource) == true) &&
                (oldPlaylist.songList.size == newPlaylist.songList.size)
        } else {
            true
        }
    }
}
