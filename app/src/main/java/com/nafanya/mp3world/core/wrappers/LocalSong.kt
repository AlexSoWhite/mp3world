package com.nafanya.mp3world.core.wrappers

import android.net.Uri

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