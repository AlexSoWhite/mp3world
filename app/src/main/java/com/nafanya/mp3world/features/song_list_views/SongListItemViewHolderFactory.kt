package com.nafanya.mp3world.features.song_list_views

import android.view.ViewGroup
import com.nafanya.mp3world.core.utils.list_utils.recycler.BaseViewHolderFactory
import com.nafanya.mp3world.features.song_list_views.base_views.SongListItemViewHolder
import com.nafanya.mp3world.features.song_list_views.song_views.AddableRemovableSongView
import com.nafanya.mp3world.features.song_list_views.song_views.AddableRemovableSongViewHolder
import com.nafanya.mp3world.features.song_list_views.song_views.ImmutableLocalSongView
import com.nafanya.mp3world.features.song_list_views.song_views.ImmutableLocalSongViewHolder
import com.nafanya.mp3world.features.song_list_views.song_views.RearrangeableSongView
import com.nafanya.mp3world.features.song_list_views.song_views.RearrangeableSongViewHolder
import com.nafanya.mp3world.features.song_list_views.song_views.RemoteSongView
import com.nafanya.mp3world.features.song_list_views.song_views.RemoteSongViewHolder
import com.nafanya.mp3world.features.song_list_views.top_button_views.ConfirmChangesButtonView
import com.nafanya.mp3world.features.song_list_views.top_button_views.ConfirmChangesButtonViewHolder
import com.nafanya.mp3world.features.song_list_views.top_button_views.ModifyPlaylistButtonView
import com.nafanya.mp3world.features.song_list_views.top_button_views.ModifyPlaylistButtonViewHolder

class SongListItemViewHolderFactory : BaseViewHolderFactory<SongListItemViewHolder> {

    override fun create(
        @ListItemType viewType: Int,
        parent: ViewGroup
    ): SongListItemViewHolder = with(parent) {
        return when (viewType) {
            SONG_LOCAL_IMMUTABLE -> ImmutableLocalSongViewHolder(ImmutableLocalSongView(context))
            SONG_ADDABLE_REMOVABLE -> {
                AddableRemovableSongViewHolder(AddableRemovableSongView(context))
            }
            SONG_REARRANGEABLE -> RearrangeableSongViewHolder(RearrangeableSongView(context))
            SONG_REMOTE -> RemoteSongViewHolder(RemoteSongView(context))
            LOADER -> ImmutableLocalSongViewHolder(ImmutableLocalSongView(context))
            DATE -> DateViewHolder(DateView(context))
            MODIFY_PLAYLIST_BUTTON -> {
                ModifyPlaylistButtonViewHolder(ModifyPlaylistButtonView(context))
            }
            CONFIRM_CHANGES_BUTTON -> {
                ConfirmChangesButtonViewHolder(ConfirmChangesButtonView(context))
            }
            else -> ImmutableLocalSongViewHolder(ImmutableLocalSongView(context))
        }
    }
}
