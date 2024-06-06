package com.nafanya.mp3world.features.songListViews

import android.view.ViewGroup
import com.nafanya.mp3world.core.utils.listUtils.recycler.BaseViewHolderFactory
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder
import com.nafanya.mp3world.features.songListViews.songViews.AddableRemovableSongView
import com.nafanya.mp3world.features.songListViews.songViews.AddableRemovableSongViewHolder
import com.nafanya.mp3world.features.songListViews.songViews.ImmutableLocalSongView
import com.nafanya.mp3world.features.songListViews.songViews.ImmutableLocalSongViewHolder
import com.nafanya.mp3world.features.songListViews.songViews.RearrangeableSongView
import com.nafanya.mp3world.features.songListViews.songViews.RearrangeableSongViewHolder
import com.nafanya.mp3world.features.songListViews.songViews.RemoteSongView
import com.nafanya.mp3world.features.songListViews.songViews.RemoteSongViewHolder
import com.nafanya.mp3world.features.songListViews.topButtonViews.ConfirmChangesButtonView
import com.nafanya.mp3world.features.songListViews.topButtonViews.ConfirmChangesButtonViewHolder
import com.nafanya.mp3world.features.songListViews.topButtonViews.ModifyPlaylistButtonView
import com.nafanya.mp3world.features.songListViews.topButtonViews.ModifyPlaylistButtonViewHolder

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
