package com.nafanya.mp3world.features.allPlaylists.view.mutablePlaylist

import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.core.wrappers.LocalSong
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.CONFIRM_CHANGES_BUTTON
import com.nafanya.mp3world.features.songListViews.MODIFY_PLAYLIST_BUTTON
import com.nafanya.mp3world.features.songListViews.SONG_REARRANGEABLE
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder
import com.nafanya.mp3world.features.songListViews.baseViews.SongView
import com.nafanya.mp3world.features.songListViews.songViews.RearrangeableSongViewHolder
import com.nafanya.mp3world.features.songListViews.topButtonViews.ConfirmChangesButtonViewHolder
import com.nafanya.mp3world.features.songListViews.topButtonViews.ModifyPlaylistButtonViewHolder

class MutablePlaylistAdapter : BaseSongListAdapter() {

    lateinit var onSongClickCallback: (SongWrapper, SongView) -> Unit
    lateinit var onModifyButtonClickCallback: () -> Unit
    lateinit var onLongPressCallback: (SongWrapper) -> Unit
    lateinit var onConfirmChangesCallback: () -> Unit
    lateinit var onActionClickCallback: (LocalSong) -> Unit

    override fun onBindViewHolder(
        holder: SongListItemViewHolder,
        position: Int
    ) {
        when (holder.itemViewType) {
            SONG_REARRANGEABLE -> {
                with(holder as RearrangeableSongViewHolder) {
                    val song = currentList[position].getDataAsSong()
                    bind(
                        song,
                        onClickCallback = { view -> onSongClickCallback(song, view) },
                        onActionClickedCallback = { onActionClickCallback(song as LocalSong) }
                    )
                    holder.attachToScreen(onLongPressCallback)
                }
            }
            MODIFY_PLAYLIST_BUTTON -> {
                with(holder as ModifyPlaylistButtonViewHolder) {
                    bind { onModifyButtonClickCallback() }
                }
            }
            CONFIRM_CHANGES_BUTTON -> {
                with(holder as ConfirmChangesButtonViewHolder) {
                    bind { onConfirmChangesCallback() }
                }
            }
        }
        super.onBindViewHolder(holder, position)
    }
}
