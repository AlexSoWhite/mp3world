package com.nafanya.mp3world.core.wrappers.song.remote

import android.net.Uri
import com.nafanya.mp3world.core.wrappers.song.SongWrapper

data class RemoteSong(
    override val uri: Uri,
    override val title: String,
    override val artist: String,
    override val duration: Long,
    val artUrl: String
) : SongWrapper(uri, title, artist, duration)
