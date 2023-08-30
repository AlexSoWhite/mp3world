package com.nafanya.mp3world.core.wrappers.remote

import android.net.Uri
import com.nafanya.mp3world.core.wrappers.SongWrapper

class RemoteSong(
    uri: Uri,
    title: String,
    artist: String,
    duration: Long,
    val artUrl: String
) : SongWrapper(uri, title, artist, duration)
