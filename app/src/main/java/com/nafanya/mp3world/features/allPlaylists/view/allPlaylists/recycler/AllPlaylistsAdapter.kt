package com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler

import com.nafanya.mp3world.core.listUtils.recycler.BaseAdapter
import com.nafanya.mp3world.core.listUtils.recycler.BaseViewHolderFactory
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.views.AddPlaylistButtonViewHolder
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.views.PlaylistViewHolder

enum class ClickType {
    CLICK,
    LONG
}

class AllPlaylistsAdapter(
    private val onAddPlaylistClickCallback: () -> Unit,
    private val onPlaylistClickCallback: (PlaylistWrapper, ClickType) -> Unit
) : BaseAdapter<AllPlaylistsListItem, AllPlaylistsItemViewHolder>(PlaylistDiffUtilCallback) {

    private val factory = AllPlaylistsViewHolderFactory()
    override val viewHolderFactory: BaseViewHolderFactory<AllPlaylistsItemViewHolder>
        get() = factory

    override fun onBindViewHolder(
        holder: AllPlaylistsItemViewHolder,
        position: Int
    ) {
        when (getItemViewType(position)) {
            ADD_PLAYLIST_BUTTON -> {
                (holder as AddPlaylistButtonViewHolder).bind { onAddPlaylistClickCallback() }
            }
            PLAYLIST -> {
                val playlist = getItem(position).getDataAsPlaylist()
                (holder as PlaylistViewHolder).bind(playlist) { clickType ->
                    onPlaylistClickCallback(playlist, clickType)
                }
            }
        }
    }
}
