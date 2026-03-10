package com.nafanya.mp3world.features.user_playlists.presentation.view_playlists.recycler

import android.view.ViewGroup
import com.nafanya.mp3world.core.utils.listUtils.recycler.BaseViewHolderFactory
import com.nafanya.mp3world.features.user_playlists.presentation.view_playlists.recycler.views.AddPlaylistButtonViewHolder
import com.nafanya.mp3world.features.user_playlists.presentation.view_playlists.recycler.views.AddPlaylistsButton
import com.nafanya.mp3world.features.user_playlists.presentation.view_playlists.recycler.views.PlaylistView
import com.nafanya.mp3world.features.user_playlists.presentation.view_playlists.recycler.views.PlaylistViewHolder
import java.lang.IllegalArgumentException

class AllPlaylistsViewHolderFactory : BaseViewHolderFactory<AllPlaylistsItemViewHolder> {

    override fun create(
        @PlaylistListItemViewType viewType: Int,
        parent: ViewGroup
    ): AllPlaylistsItemViewHolder {
        return when (viewType) {
            ADD_PLAYLIST_BUTTON -> {
                AddPlaylistButtonViewHolder(AddPlaylistsButton(parent.context))
            }
            PLAYLIST -> {
                PlaylistViewHolder(PlaylistView(parent.context))
            }
            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }
    }
}
