package com.nafanya.mp3world.features.remoteSongs.view

import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.SONG_REMOTE
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder
import com.nafanya.mp3world.features.songListViews.songViews.RemoteSongViewHolder

class RemoteSongsAdapter(
    private val onSongClickCallback: (SongWrapper) -> Unit,
    private val onActionClickCallback: (SongWrapper) -> Unit
) : BaseSongListAdapter() {

    override fun onBindViewHolder(holder: SongListItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (getItemViewType(position) == SONG_REMOTE) {
            val song = getItem(position).getDataAsSong()
            (holder as RemoteSongViewHolder).bind(
                song,
                onClickCallBack = { onSongClickCallback(song) },
                onActionClickedCallback = { onActionClickCallback(song) }
            )
        }
    }
}
