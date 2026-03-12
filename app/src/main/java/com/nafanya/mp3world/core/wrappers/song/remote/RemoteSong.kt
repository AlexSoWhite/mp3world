package com.nafanya.mp3world.core.wrappers.song.remote

import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import com.nafanya.mp3world.core.wrappers.song.SongType
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.player.Song

data class RemoteSong(
    override val uri: Uri,
    override val title: String,
    override val artist: String,
    override val duration: Long,
    val artUrl: String
) : SongWrapper(uri, title, artist, duration) {

    override fun getSongType(): Int = SongType.REMOTE.ordinal

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
                            putString(ART_URL_KEY, artUrl)
                        }
                    )
                    .build()
            ).build()
    }

    companion object {
        private const val ART_URL_KEY = "ART_URL_KEY"

        fun fromMediaItem(mediaItem: MediaItem): Song? {
            return try {
                val metadata = mediaItem.mediaMetadata
                val extras = metadata.extras!!
                return RemoteSong(
                    uri = extras.getParcelable(URI_KEY)!!, // from Song
                    title = metadata.title!!.toString(), // from SongWrapper
                    artist = metadata.artist!!.toString(), // from SongWrapper
                    duration = extras.getLong(DURATION_KEY), // from SongWrapper
                    artUrl = extras.getString(ART_URL_KEY)!!
                )
            } catch (exception: NullPointerException) {
                Log.d("RemoteSong", "exception during conversion from media item: $exception")
                null
            }
        }
    }
}
