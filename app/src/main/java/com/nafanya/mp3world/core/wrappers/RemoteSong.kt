package com.nafanya.mp3world.core.wrappers

import android.net.Uri

class RemoteSong(
    uri: Uri,
    title: String,
    artist: String,
    duration: Long,
    val artUrl: String
) : SongWrapper(uri, title, artist, duration)
