package com.nafanya.mp3world.core.wrappers

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.coroutines.DefaultCoroutineProvider
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import java.io.IOException
import java.net.URL
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ArtFactory @Inject constructor(
    private val context: Context,
    private val defaultCoroutineProvider: DefaultCoroutineProvider,
    private val ioCoroutineProvider: IOCoroutineProvider
) {

    companion object {
        private const val artDimension = 500
    }

    val defaultBitmap: Bitmap

    init {
        val options = BitmapFactory.Options()
        options.outWidth = artDimension
        options.outHeight = artDimension
        defaultBitmap = AppCompatResources.getDrawable(
            context,
            R.drawable.song_icon_preview
        )!!.toBitmap(
            artDimension,
            artDimension
        )
    }

    /**
     * Returns [SongWrapper.art] for local song with given [uri]
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    @Synchronized
    fun createBitmap(uri: Uri): SharedFlow<Bitmap> {
        val flow = MutableSharedFlow<Bitmap>()
        ioCoroutineProvider.ioScope.launch {
            kotlin.runCatching {
                try {
                    defaultCoroutineProvider.defaultScope.launch {
                        kotlin.runCatching {
                            val bitmap = context
                                .contentResolver
                                ?.loadThumbnail(
                                    uri,
                                    Size(artDimension, artDimension),
                                    null
                                )!!
                            flow.emit(bitmap)
                        }
                    }
                } catch (exception: IOException) {
                    /**
                     * TODO: logging
                     */
                }
            }
        }
        return flow
    }

    @Synchronized
    fun createBitmap(albumId: Long): SharedFlow<Bitmap> {
        val artworkUri = Uri.parse("content://media/external/audio/albumart")
        val albumUri = ContentUris.withAppendedId(artworkUri, albumId)
        val flow = MutableSharedFlow<Bitmap>()
        ioCoroutineProvider.ioScope.launch {
            kotlin.runCatching {
                defaultCoroutineProvider.defaultScope.launch {
                    kotlin.runCatching {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            try {
                                val source = ImageDecoder.createSource(
                                    context.contentResolver!!,
                                    albumUri
                                )
                                val bitmap = ImageDecoder.decodeBitmap(source)
                                flow.emit(bitmap)
                            } catch (exception: IOException) {
                                /**
                                 * TODO: logging
                                 */
                            }
                        } else try {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                context.contentResolver,
                                albumUri
                            )
                            flow.emit(bitmap)
                        } catch (exception: IOException) {
                            /**
                             * TODO: logging
                             */
                        }
                    }
                }
            }
        }
        return flow
    }

    /**
     * Returns [SongWrapper.art] for remote songs with given [url]
     */
    @Synchronized
    fun createBitmap(url: String): SharedFlow<Bitmap> {
        val flow = MutableSharedFlow<Bitmap>()
        ioCoroutineProvider.ioScope.launch {
            kotlin.runCatching {
                val connection = URL(url).openConnection()
                defaultCoroutineProvider.defaultScope.launch {
                    kotlin.runCatching {
                        val bitmap = BitmapFactory.decodeStream(connection.getInputStream())
                        flow.emit(bitmap)
                    }
                }
            }
        }
        return flow
    }
}
