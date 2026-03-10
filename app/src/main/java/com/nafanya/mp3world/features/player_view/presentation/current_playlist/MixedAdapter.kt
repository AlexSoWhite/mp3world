package com.nafanya.mp3world.features.player_view.presentation.current_playlist

import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.features.playlist.base_views.BaseSongListAdapter
import com.nafanya.mp3world.features.song_list_views.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.song_list_views.SONG_REMOTE
import com.nafanya.mp3world.features.song_list_views.base_views.SongListItemViewHolder
import com.nafanya.mp3world.features.song_list_views.base_views.SongView
import com.nafanya.mp3world.features.song_list_views.song_views.ImmutableLocalSongViewHolder
import com.nafanya.mp3world.features.song_list_views.song_views.RemoteSongViewHolder
import com.nafanya.player.Song

class MixedAdapter : BaseSongListAdapter() {

    lateinit var onClickCallback: (SongView, Song) -> Unit

    lateinit var onLocalActionClickCallback: (LocalSong) -> Unit

    lateinit var onRemoteActionClickCallback: (RemoteSong) -> Unit

    override fun onBindViewHolder(holder: SongListItemViewHolder, position: Int) {
        val song = currentList[position].getDataAsSong()
        when (holder.itemViewType) {
            SONG_LOCAL_IMMUTABLE -> {
                (holder as ImmutableLocalSongViewHolder).bind(
                    song,
                    onClickCallback = {
                        onClickCallback(it, song)
                    },
                    onActionClickedCallback = {
                        onLocalActionClickCallback.invoke(it as LocalSong)
                    }
                )
            }
            SONG_REMOTE -> {
                (holder as RemoteSongViewHolder).bind(
                    song,
                    onClickCallback = {
                        onClickCallback(it, song)
                    },
                    onActionClickedCallback = {
                        onRemoteActionClickCallback.invoke(it as RemoteSong)
                    }
                )
            }
        }
        super.onBindViewHolder(holder, position)
    }
}
