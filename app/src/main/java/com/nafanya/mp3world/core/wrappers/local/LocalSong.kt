package com.nafanya.mp3world.core.wrappers.local

import android.graphics.Bitmap
import android.net.Uri
import com.nafanya.mp3world.core.wrappers.SongWrapper

@Suppress("LongParameterList")
class LocalSong(
    uri: Uri,
    art: Bitmap,
    title: String,
    artist: String,
    duration: Long,
    val date: Long,
    val artistId: Long,
    val albumId: Long,
    val album: String
) : SongWrapper(
    uri,
    art,
    title,
    artist,
    duration
)
