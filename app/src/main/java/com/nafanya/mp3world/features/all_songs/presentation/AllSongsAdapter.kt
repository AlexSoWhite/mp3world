package com.nafanya.mp3world.features.all_songs.presentation

import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.features.playlist.base_views.BaseSongListAdapter
import com.nafanya.mp3world.features.song_list_views.DATE
import com.nafanya.mp3world.features.song_list_views.DateViewHolder
import com.nafanya.mp3world.features.song_list_views.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.song_list_views.base_views.SongListItemViewHolder
import com.nafanya.mp3world.features.song_list_views.base_views.SongView
import com.nafanya.mp3world.features.song_list_views.song_views.ImmutableLocalSongViewHolder

class AllSongsAdapter(
    private val onSongItemClickCallback: (SongWrapper, SongView) -> Unit,
    private val onActionClickedCallback: (LocalSong) -> Unit
) : BaseSongListAdapter() {

    override fun onBindViewHolder(holder: SongListItemViewHolder, position: Int) {
        when (holder.itemViewType) {
            SONG_LOCAL_IMMUTABLE -> {
                val song = currentList[position].getDataAsSong()
                (holder as ImmutableLocalSongViewHolder).bind(
                    song,
                    onClickCallback = { view -> onSongItemClickCallback(song, view) },
                    onActionClickedCallback = { onActionClickedCallback(song as LocalSong) }
                )
            }
            DATE -> {
                val date = currentList[position].getDataAsDate()
                (holder as DateViewHolder).bind(date)
            }
        }
        super.onBindViewHolder(holder, position)
    }
}
