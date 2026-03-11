package com.nafanya.mp3world.presentation.user_playlists.modify_playlist

import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.presentation.playlist.base_views.BaseSongListAdapter
import com.nafanya.mp3world.presentation.song_list_views.SONG_ADDABLE_REMOVABLE
import com.nafanya.mp3world.presentation.song_list_views.base_views.SongListItemViewHolder
import com.nafanya.mp3world.presentation.song_list_views.base_views.SongView
import com.nafanya.mp3world.presentation.song_list_views.song_views.AddableRemovableSongViewHolder

class ModifyPlaylistAdapter(
    private var modifyingPlaylist: PlaylistWrapper? = null,
    private val onSongClickCallback: (SongWrapper, SongView) -> Unit,
    private val onSongAddCallback: (SongWrapper) -> Unit,
    private val onSongRemoveCallback: (SongWrapper) -> Unit
) : BaseSongListAdapter() {

    fun setModifyingPlaylist(playlist: PlaylistWrapper) {
        modifyingPlaylist = playlist
    }

    override fun onBindViewHolder(holder: SongListItemViewHolder, position: Int) {
        if (holder.itemViewType == SONG_ADDABLE_REMOVABLE) {
            with(holder as AddableRemovableSongViewHolder) {
                val song = getItem(position).getDataAsSong()
                bind(song, onClickCallback = { view -> onSongClickCallback(song, view) })
                modifyingPlaylist?.let {
                    attachToPlaylist(it, onSongAddCallback, onSongRemoveCallback)
                }
            }
        }
        super.onBindViewHolder(holder, position)
    }
}
