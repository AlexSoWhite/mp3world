package com.nafanya.mp3world.core.wrappers.remote

import android.net.Uri
import com.nafanya.mp3world.core.wrappers.SongWrapper
import java.security.MessageDigest

class RemoteSong(
    uri: Uri,
    art: Uri,
    title: String,
    artist: String,
    duration: Long
) : SongWrapper(uri, art, title, artist, duration) {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(uri.toString().toByteArray())
    }
}
