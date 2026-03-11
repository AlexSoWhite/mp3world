package com.nafanya.mp3world.presentation.user_playlists.mutable_playlist

import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.presentation.playlist.base_views.BaseSongListAdapter
import com.nafanya.mp3world.presentation.song_list_views.CONFIRM_CHANGES_BUTTON
import com.nafanya.mp3world.presentation.song_list_views.MODIFY_PLAYLIST_BUTTON
import com.nafanya.mp3world.presentation.song_list_views.SONG_REARRANGEABLE
import com.nafanya.mp3world.presentation.song_list_views.base_views.SongListItemViewHolder
import com.nafanya.mp3world.presentation.song_list_views.base_views.SongView
import com.nafanya.mp3world.presentation.song_list_views.song_views.RearrangeableSongViewHolder
import com.nafanya.mp3world.presentation.song_list_views.top_button_views.ConfirmChangesButtonViewHolder
import com.nafanya.mp3world.presentation.song_list_views.top_button_views.ModifyPlaylistButtonViewHolder

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
