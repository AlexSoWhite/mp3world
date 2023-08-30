package com.nafanya.mp3world.core.wrappers.local

import android.net.Uri
import com.nafanya.mp3world.core.wrappers.SongWrapper
import java.security.MessageDigest

@Suppress("LongParameterList")
class LocalSong(
    uri: Uri,
    art: Uri,
    title: String,
    artist: String,
    duration: Long,
    val date: Long,
    val artistId: Long,
    val albumId: Long,
    val album: String
) : SongWrapper(uri, art, title, artist, duration) {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(uri.toString().toByteArray())
    }
}
