package com.nafanya.mp3world.core.wrappers.remote

import android.graphics.Bitmap
import android.net.Uri
import com.nafanya.mp3world.core.wrappers.SongWrapper

class RemoteSong(
    uri: Uri,
    art: Bitmap,
    title: String,
    artist: String,
    duration: Long
) : SongWrapper(
    uri,
    art,
    title,
    artist,
    duration
)
