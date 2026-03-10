package com.nafanya.mp3world.features.remote_songs.presentation

import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.features.playlist.base_views.BaseSongListAdapter
import com.nafanya.mp3world.features.song_list_views.SONG_REMOTE
import com.nafanya.mp3world.features.song_list_views.base_views.SongListItemViewHolder
import com.nafanya.mp3world.features.song_list_views.base_views.SongView
import com.nafanya.mp3world.features.song_list_views.song_views.RemoteSongViewHolder

class RemoteSongsAdapter(
    private val onSongClickCallback: (SongWrapper, SongView) -> Unit,
    private val onActionClickCallback: (SongWrapper) -> Unit
) : BaseSongListAdapter() {

    override fun onBindViewHolder(holder: SongListItemViewHolder, position: Int) {
        if (getItemViewType(position) == SONG_REMOTE) {
            val song = getItem(position).getDataAsSong()
            (holder as RemoteSongViewHolder).bind(
                song,
                onClickCallback = { view -> onSongClickCallback(song, view) },
                onActionClickedCallback = { onActionClickCallback(song) }
            )
        }
        super.onBindViewHolder(holder, position)
    }
}
