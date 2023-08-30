package com.nafanya.mp3world.core.wrappers

import android.graphics.Bitmap
import android.util.Size
import kotlinx.coroutines.flow.Flow

interface SongImageBitmapFactory {

    fun getBitmapForSong(song: SongWrapper, size: Size? = null): Flow<Bitmap?>
}
