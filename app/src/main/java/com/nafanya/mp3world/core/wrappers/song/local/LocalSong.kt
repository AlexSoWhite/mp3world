package com.nafanya.mp3world.core.wrappers.song.local

import android.net.Uri
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import com.nafanya.mp3world.core.wrappers.song.SongType
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.player.Song

@Suppress("LongParameterList")
data class LocalSong(
    override val uri: Uri,
    override val title: String,
    override val artist: String,
    override val duration: Long,
    val date: Long,
    val artistId: Long,
    val albumId: Long,
    val album: String
) : SongWrapper(uri, title, artist, duration) {

    override fun getSongType(): Int = SongType.LOCAL.ordinal

    override fun toMediaItem(): MediaItem {
        val baseMediaItem = super.toMediaItem()
        val baseMetadata = baseMediaItem.mediaMetadata
        val baseExtras = baseMetadata.extras ?: bundleOf()
        return baseMediaItem.buildUpon()
            .setMediaMetadata(
                baseMetadata.buildUpon()
                    .setExtras(
                        Bundle().apply {
                            putAll(baseExtras)
                            putLong(DATE_KEY, date)
                            putLong(ARTIST_ID_KEY, artistId)
                            putLong(ALBUM_ID_KEY, albumId)
                        }
                    )
                    .setAlbumTitle(album)
                    .build()
            ).build()
    }

    companion object {
        private const val DATE_KEY = "DATE_KEY"
        private const val ARTIST_ID_KEY = "ARTIST_ID_KEY"
        private const val ALBUM_ID_KEY = "ALBUM_ID_KEY"

        fun fromMediaItem(mediaItem: MediaItem): Song? {
            return try {
                val metadata = mediaItem.mediaMetadata
                val extras = metadata.extras!!
                LocalSong(
                    uri = extras.getParcelable(URI_KEY)!!, // from Song
                    title = metadata.title!!.toString(), // from SongWrapper
                    artist = metadata.artist!!.toString(), // from SongWrapper
                    duration = extras.getLong(DURATION_KEY), // from SongWrapper
                    date = extras.getLong(DATE_KEY),
                    artistId = extras.getLong(ARTIST_ID_KEY),
                    albumId = extras.getLong(ALBUM_ID_KEY),
                    album = metadata.albumTitle!!.toString(),
                )
            } catch (exception: NullPointerException) {
                Log.d("LocalSong", "exception during conversion from media item: $exception")
                null
            }
        }
    }
}
