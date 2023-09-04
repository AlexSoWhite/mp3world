package com.nafanya.mp3world.features.allSongs

import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import kotlinx.coroutines.flow.Flow

interface SongListManager : ListManager {

    val songList: Flow<List<LocalSong>>
}

// TODO: string res
fun List<SongWrapper>.asAllSongsPlaylist(): PlaylistWrapper {
    return PlaylistWrapper(
        name = "Мои песни",
        songList = this
    )
}
