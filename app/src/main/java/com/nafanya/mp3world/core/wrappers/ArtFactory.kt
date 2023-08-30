package com.nafanya.mp3world.core.wrappers

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi

interface ArtFactory {

    fun createArtUri(url: String): Uri

    fun createArtUri(albumId: Long): Uri

    @RequiresApi(Build.VERSION_CODES.Q)
    fun createArtUri(uri: Uri): Uri
}
