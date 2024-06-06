package com.nafanya.mp3world.core.wrappers.images

import android.graphics.Bitmap
import android.util.Size
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import kotlinx.coroutines.flow.Flow

interface SongImageBitmapFactory {

    fun getBitmapForSong(song: SongWrapper, size: Size? = null): Flow<Bitmap>
}
