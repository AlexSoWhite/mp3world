package com.nafanya.mp3world.core.wrappers.song.local

import android.net.Uri
import com.nafanya.mp3world.core.wrappers.song.SongWrapper

@Suppress("LongParameterList")
data class LocalSong(
    override val uri: Uri,
    override val title: String,
    override val artist: String,
    override val duration: Long,
    val date: Long,
    val artistId: Long,
    val albumId: Long,
    val album: String
) : SongWrapper(uri, title, artist, duration)
