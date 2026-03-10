package com.nafanya.mp3world.features.all_songs.domain

import com.nafanya.mp3world.core.list_managers.PlaylistProvider
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import kotlinx.coroutines.flow.Flow

interface SongPlaylistProvider : PlaylistProvider {

    val songList: Flow<List<LocalSong>>
}

// TODO: string res
fun List<SongWrapper>.asAllSongsPlaylist(): PlaylistWrapper {
    return PlaylistWrapper(
        name = "Мои песни",
        songList = this
    )
}
