package com.nafanya.mp3world.features.allPlaylists.view.mutablePlaylist

import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.CONFIRM_CHANGES_BUTTON
import com.nafanya.mp3world.features.songListViews.MODIFY_PLAYLIST_BUTTON
import com.nafanya.mp3world.features.songListViews.SONG_REARRANGEABLE
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder
import com.nafanya.mp3world.features.songListViews.songViews.RearrangeableSongViewHolder
import com.nafanya.mp3world.features.songListViews.topButtonViews.ConfirmChangesButtonViewHolder
import com.nafanya.mp3world.features.songListViews.topButtonViews.ModifyPlaylistButtonViewHolder

class MutablePlaylistAdapter(
    private val onSongClickCallback: (SongWrapper) -> Unit,
    private val onModifyButtonClickCallback: () -> Unit,
    private val onLongPressCallback: (SongWrapper) -> Unit,
    private val onConfirmChangesCallback: () -> Unit,
    private val onActionClickCallback: (LocalSong) -> Unit
) : BaseSongListAdapter() {

    override fun onBindViewHolder(
        holder: SongListItemViewHolder,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)
        when (holder.itemViewType) {
            SONG_REARRANGEABLE -> {
                with(holder as RearrangeableSongViewHolder) {
                    val song = currentList[position].getDataAsSong()
                    bind(
                        song,
                        onClickCallBack = { onSongClickCallback(song) },
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
    }
}
