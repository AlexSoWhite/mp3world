package com.nafanya.mp3world.features.playlist.immutablePlaylist

import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder
import com.nafanya.mp3world.features.songListViews.baseViews.SongView
import com.nafanya.mp3world.features.songListViews.songViews.ImmutableLocalSongViewHolder

class ImmutablePlaylistAdapter(
    private val onSongClickCallback: (SongWrapper, SongView) -> Unit,
    private val onActionClickedCallback: (LocalSong) -> Unit
) : BaseSongListAdapter() {

    override fun onBindViewHolder(holder: SongListItemViewHolder, position: Int) {
        val song = getItem(position).getDataAsSong()
        (holder as ImmutableLocalSongViewHolder).bind(
            song,
            onClickCallback = { view -> onSongClickCallback(song, view) },
            onActionClickedCallback = { onActionClickedCallback(song as LocalSong) }
        )
        super.onBindViewHolder(holder, position)
    }
}
