package com.nafanya.mp3world.features.allPlaylists.view.modifyPlaylist

import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.SONG_ADDABLE_REMOVABLE
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder
import com.nafanya.mp3world.features.songListViews.songViews.AddableRemovableSongViewHolder

class ModifyPlaylistAdapter(
    private var modifyingPlaylist: PlaylistWrapper? = null,
    private val onSongClickCallback: (SongWrapper) -> Unit,
    private val onSongAddCallback: (SongWrapper) -> Unit,
    private val onSongRemoveCallback: (SongWrapper) -> Unit
) : BaseSongListAdapter() {

    fun setModifyingPlaylist(playlist: PlaylistWrapper) {
        modifyingPlaylist = playlist
    }

    override fun onBindViewHolder(holder: SongListItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder.itemViewType == SONG_ADDABLE_REMOVABLE) {
            with(holder as AddableRemovableSongViewHolder) {
                val song = getItem(position).getDataAsSong()
                bind(song, onClickCallBack = { onSongClickCallback(song) })
                modifyingPlaylist?.let {
                    attachToPlaylist(it, onSongAddCallback, onSongRemoveCallback)
                }
            }
        }
    }
}
