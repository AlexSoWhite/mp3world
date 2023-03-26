package com.nafanya.mp3world.features.remoteSongs.view

import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.SONG_REMOTE
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder
import com.nafanya.mp3world.features.songListViews.baseViews.SongView
import com.nafanya.mp3world.features.songListViews.songViews.RemoteSongViewHolder

class RemoteSongsAdapter(
    private val onSongClickCallback: (SongWrapper, SongView) -> Unit,
    private val onActionClickCallback: (SongWrapper) -> Unit
) : BaseSongListAdapter() {

    override fun onBindViewHolder(holder: SongListItemViewHolder, position: Int) {
        if (getItemViewType(position) == SONG_REMOTE) {
            val song = getItem(position).getDataAsSong()
            (holder as RemoteSongViewHolder).bind(
                song,
                onClickCallBack = { view -> onSongClickCallback(song, view) },
                onActionClickedCallback = { onActionClickCallback(song) }
            )
        }
        super.onBindViewHolder(holder, position)
    }
}
