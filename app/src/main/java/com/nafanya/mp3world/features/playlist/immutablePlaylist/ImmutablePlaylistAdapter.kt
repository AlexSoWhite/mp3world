package com.nafanya.mp3world.features.playlist.immutablePlaylist

import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder
import com.nafanya.mp3world.features.songListViews.songViews.ImmutableLocalSongViewHolder

class ImmutablePlaylistAdapter(
    private val onSongClickCallback: (SongWrapper) -> Unit,
    private val onActionClickedCallback: (LocalSong) -> Unit
) : BaseSongListAdapter() {

    override fun onBindViewHolder(holder: SongListItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val song = getItem(position).getDataAsSong()
        (holder as ImmutableLocalSongViewHolder).bind(
            song,
            onClickCallBack = { onSongClickCallback(song) },
            onActionClickedCallback = { onActionClickedCallback(song as LocalSong) }
        )
    }
}
