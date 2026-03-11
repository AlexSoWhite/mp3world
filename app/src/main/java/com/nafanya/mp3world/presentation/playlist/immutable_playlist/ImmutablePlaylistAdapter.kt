package com.nafanya.mp3world.presentation.playlist.immutable_playlist

import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.presentation.playlist.base_views.BaseSongListAdapter
import com.nafanya.mp3world.presentation.song_list_views.base_views.SongListItemViewHolder
import com.nafanya.mp3world.presentation.song_list_views.base_views.SongView
import com.nafanya.mp3world.presentation.song_list_views.song_views.ImmutableLocalSongViewHolder

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
