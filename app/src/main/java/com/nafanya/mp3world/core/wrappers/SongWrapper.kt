package com.nafanya.mp3world.core.wrappers

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.core.net.toUri
import com.nafanya.mp3world.R
import com.nafanya.player.Song
import java.io.IOException
import java.net.URL

abstract class SongWrapper(
    uri: Uri,
    val art: Bitmap,
    val title: String,
    val artist: String,
    val duration: Long
) : Song(uri) {

    override fun equals(other: Any?): Boolean {
        return (this.uri == (other as SongWrapper).uri)
    }

    override fun hashCode(): Int {
        var result = art.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + duration.hashCode()
        return result
    }
}

class ArtFactory(private val context: Context? = null) {

    companion object {
        private const val artDimension = 1024
    }

    private val defaultBitmap by lazy {
        val options = BitmapFactory.Options()
        options.outWidth = artDimension
        options.outHeight = artDimension
        BitmapFactory.decodeResource(
            context?.resources,
            R.drawable.song_icon_preview,
            options
        )
    }

    /**
     * Returns [SongWrapper.art] for local song with given [uri]
     */
    fun createBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                context
                    ?.contentResolver
                    ?.loadThumbnail(
                        uri,
                        Size(artDimension, artDimension),
                        null
                    )
            } catch (exception: IOException) {
                defaultBitmap
            }
        } else {
            defaultBitmap
        }!!
    }

    /**
     * Returns [SongWrapper.art] for remote songs with given [url]
     */
    fun createBitmap(url: String): Bitmap {
        return BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
    }
}

class UriFactory {

    /**
     * Returns [Uri] for [AoedePlayer] for local song with given [id]
     */
    fun getUri(id: Long): Uri {
        return ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            id
        )
    }

    /**
     * Returns [Uri] for [AoedePlayer] for remote song with given [url]
     */
    fun getUri(url: String): Uri {
        return url.toUri()
    }
}
