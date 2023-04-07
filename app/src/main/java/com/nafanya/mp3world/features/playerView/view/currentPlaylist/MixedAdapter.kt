package com.nafanya.mp3world.features.playerView.view.currentPlaylist

import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.songListViews.SONG_REMOTE
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder
import com.nafanya.mp3world.features.songListViews.baseViews.SongView
import com.nafanya.mp3world.features.songListViews.songViews.ImmutableLocalSongViewHolder
import com.nafanya.mp3world.features.songListViews.songViews.RemoteSongViewHolder
import com.nafanya.player.Song

class MixedAdapter : BaseSongListAdapter() {

    lateinit var onClickCallback: (SongView, Song) -> Unit

    lateinit var onLocalActionClickCallback: (Song) -> Unit

    lateinit var onRemoteActionClickCallback: (Song) -> Unit

    override fun onBindViewHolder(holder: SongListItemViewHolder, position: Int) {
        val song = currentList[position].getDataAsSong()
        when (holder.itemViewType) {
            SONG_LOCAL_IMMUTABLE -> {
                (holder as ImmutableLocalSongViewHolder).bind(
                    song,
                    onClickCallback = {
                        onClickCallback(it, song)
                    },
                    onLocalActionClickCallback
                )
            }
            SONG_REMOTE -> {
                (holder as RemoteSongViewHolder).bind(
                    song,
                    onClickCallback = {
                        onClickCallback(it, song)
                    },
                    onRemoteActionClickCallback
                )
            }
        }
        super.onBindViewHolder(holder, position)
    }
}
