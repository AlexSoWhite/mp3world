package com.nafanya.mp3world.presentation.core.images

import android.graphics.Bitmap
import android.util.Size
import com.nafanya.player.Song

interface SongImageBitmapFactory {

    suspend fun getBitmapForSong(song: Song, size: Size? = null): Bitmap
}
