package com.nafanya.mp3world.presentation.core.images

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
import com.nafanya.mp3world.core.coroutines.DispatchersProvider
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import java.io.FileNotFoundException
import java.lang.Exception
import java.lang.NullPointerException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

@Suppress("TooGenericExceptionCaught")
@Singleton
class SongImageBitmapFactoryImpl @Inject constructor(
    private val context: Context,
    private val okHttpClient: OkHttpClient,
    private val dispatchersProvider: DispatchersProvider
) : SongImageBitmapFactory {

    companion object {
        private const val ART_DIMENSION = 500
    }

    private val defaultBitmap: Bitmap

    init {
        val options = BitmapFactory.Options()
        options.outWidth = ART_DIMENSION
        options.outHeight = ART_DIMENSION
        defaultBitmap = AppCompatResources.getDrawable(
            context,
            R.drawable.song_icon_preview
        )!!.toBitmap(
            ART_DIMENSION,
            ART_DIMENSION
        )
    }

    // todo: make suspend?
    override suspend fun getBitmapForSong(song: SongWrapper, size: Size?): Bitmap {
        return when (song) {
            is RemoteSong -> getBitmapForRemoteSong(song)
            is LocalSong -> getBitmapForLocalSong(song, size ?: Size(ART_DIMENSION, ART_DIMENSION))
            else -> throw IllegalArgumentException("unknown song type ${song.javaClass}")
        }
    }

    private suspend fun getBitmapForRemoteSong(song: RemoteSong): Bitmap = withContext(dispatchersProvider.io) {
        return@withContext suspendCancellableCoroutine { continuation ->
            val request = Request.Builder().url(song.artUrl).build()
            okHttpClient.newCall(request)
                .enqueue(
                    responseCallback = object : Callback {
                        override fun onFailure(call: Call, e: java.io.IOException) {
                            continuation.resume(defaultBitmap)
                        }

                        override fun onResponse(call: Call, response: Response) {
                            launch {
                                withContext(dispatchersProvider.default) {
                                    val bitmap = BitmapFactory.decodeStream(response.body?.byteStream())
                                    continuation.resume(bitmap)
                                }
                            }
                        }
                    }
                )
        }
    }

    private suspend fun getBitmapForLocalSong(song: LocalSong, size: Size): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getBitmapForLocalSongFromQ(song, size)
        } else {
            getBitmapForLocalSongPreQ(song)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun getBitmapForLocalSongFromQ(song: LocalSong, size: Size): Bitmap =
        withContext(dispatchersProvider.default) {
            return@withContext try {
                val bitmap = context
                    .contentResolver
                    ?.loadThumbnail(
                        song.uri,
                        size,
                        null
                    )!!
                bitmap
            } catch (exception: IOException) {
                defaultBitmap
            } catch (exception: NullPointerException) {
                defaultBitmap
            } catch (exception: FileNotFoundException) {
                defaultBitmap
            } catch (exception: Exception) {
                defaultBitmap
            }
        }

    private suspend fun getBitmapForLocalSongPreQ(song: LocalSong): Bitmap {
        val artworkUri = Uri.parse("content://media/external/audio/albumart")
        val albumUri = ContentUris.withAppendedId(artworkUri, song.albumId)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getBitmapForLocalSongP(albumUri)
        } else {
            getBitmapForLocalSongPreP(albumUri)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private suspend fun getBitmapForLocalSongP(albumUri: Uri): Bitmap = withContext(dispatchersProvider.default) {
        return@withContext try {
            val source = ImageDecoder.createSource(
                context.contentResolver!!,
                albumUri
            )
            val bitmap = ImageDecoder.decodeBitmap(source)
            bitmap
        } catch (exception: IOException) {
            defaultBitmap
        } catch (exception: Exception) {
            defaultBitmap
        }
    }

    private suspend fun getBitmapForLocalSongPreP(albumUri: Uri): Bitmap = withContext(dispatchersProvider.io) {
        return@withContext try {
            val bitmap = MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                albumUri
            )
            bitmap
        } catch (exception: IOException) {
            defaultBitmap
        } catch (exception: Exception) {
            defaultBitmap
        }
    }
}
