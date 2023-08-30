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
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

@Singleton
class SongImageBitmapFactoryImpl @Inject constructor(
    private val context: Context,
    private val okHttpClient: OkHttpClient,
    private val ioCoroutineProvider: IOCoroutineProvider,
    private val defaultCoroutineProvider: DefaultCoroutineProvider
) : SongImageBitmapFactory {

    companion object {
        private const val artDimension = 500
    }

    private val defaultBitmap: Bitmap

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

    override fun getBitmapForSong(song: SongWrapper, size: Size?): SharedFlow<Bitmap?> {
        return when (song) {
            is RemoteSong -> getBitmapForRemoteSong(song)
            is LocalSong -> getBitmapForLocalSong(song, size ?: Size(artDimension, artDimension))
            else -> throw IllegalArgumentException("unknown song type ${song.javaClass}")
        }
    }

    private fun getBitmapForRemoteSong(song: RemoteSong): SharedFlow<Bitmap?> {
        val flow = MutableSharedFlow<Bitmap?>()
        ioCoroutineProvider.ioScope.launch {
            kotlin.runCatching {
                val request = Request.Builder().url(song.artUrl).build()
                okHttpClient.newCall(request)
                    .enqueue(
                        responseCallback = object : Callback {
                            override fun onFailure(call: Call, e: java.io.IOException) {
                                /**
                                 * TODO logging
                                 */
                                ioCoroutineProvider.ioScope.launch {
                                    flow.emit(defaultBitmap)
                                }
                            }

                            override fun onResponse(call: Call, response: Response) {
                                defaultCoroutineProvider.defaultScope.launch {
                                    val bitmap = BitmapFactory
                                        .decodeStream(
                                            response.body?.byteStream()
                                        )
                                    flow.emit(bitmap)
                                }
                            }
                        }
                    )
            }
        }
        return flow
    }

    private fun getBitmapForLocalSong(song: LocalSong, size: Size): SharedFlow<Bitmap?> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getBitmapForLocalSongFromQ(song, size)
        } else {
            getBitmapForLocalSongPreQ(song)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getBitmapForLocalSongFromQ(song: LocalSong, size: Size): SharedFlow<Bitmap?> {
        val flow = MutableSharedFlow<Bitmap?>()
        ioCoroutineProvider.ioScope.launch {
            kotlin.runCatching {
                try {
                    defaultCoroutineProvider.defaultScope.launch {
                        kotlin.runCatching {
                            val bitmap = context
                                .contentResolver
                                ?.loadThumbnail(
                                    song.uri,
                                    size,
                                    null
                                )!!
                            flow.emit(bitmap)
                        }
                    }
                } catch (exception: IOException) {
                    /**
                     * TODO: logging
                     */
                    flow.emit(defaultBitmap)
                }
            }
        }
        return flow
    }

    private fun getBitmapForLocalSongPreQ(song: LocalSong): SharedFlow<Bitmap?> {
        val artworkUri = Uri.parse("content://media/external/audio/albumart")
        val albumUri = ContentUris.withAppendedId(artworkUri, song.albumId)
        val flow = MutableSharedFlow<Bitmap?>()
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
                                flow.emit(defaultBitmap)
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
                            flow.emit(defaultBitmap)
                        }
                    }
                }
            }
        }
        return flow
    }
}
