package com.nafanya.mp3world.core.wrappers

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import javax.inject.Inject

class ArtFactoryImpl @Inject constructor(
    private val context: Context
) : ArtFactory {

    companion object {
        private const val artDimension = 500
    }

    override fun createArtUri(albumId: Long): Uri {
        val artworkUri = Uri.parse("content://media/external/audio/albumart")
        return ContentUris.withAppendedId(artworkUri, albumId)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun createArtUri(uri: Uri): Uri {
        return uri
    }

    override fun createArtUri(url: String): Uri {
        return Uri.parse(url)
    }
}
