package com.nafanya.mp3world.core.wrappers.song.local

import android.net.Uri
import com.nafanya.mp3world.core.wrappers.song.SongWrapper

@Suppress("LongParameterList")
class LocalSong(
    uri: Uri,
    title: String,
    artist: String,
    duration: Long,
    val date: Long,
    val artistId: Long,
    val albumId: Long,
    val album: String
) : SongWrapper(uri, title, artist, duration)
