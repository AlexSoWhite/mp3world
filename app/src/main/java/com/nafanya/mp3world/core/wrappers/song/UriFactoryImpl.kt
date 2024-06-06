package com.nafanya.mp3world.core.wrappers.song

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import javax.inject.Inject

class UriFactoryImpl @Inject constructor() : UriFactory {

    /**
     * Returns [Uri] for [com.nafanya.player.AoedePlayer] for local song with given [id]
     */
    override fun getUri(id: Long): Uri {
        return ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            id
        )
    }

    /**
     * Returns [Uri] for [com.nafanya.player.AoedePlayer] for remote song with given [url]
     */
    override fun getUri(url: String): Uri {
        return url.toUri()
    }
}
