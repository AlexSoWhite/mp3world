package com.nafanya.mp3world.core.wrappers.song

import android.net.Uri
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.player.Song

// for media item extras
enum class SongType {
    LOCAL,
    REMOTE
}

abstract class SongWrapper(
    uri: Uri,
    open val title: String,
    open val artist: String,
    open val duration: Long
) : Song(uri) {

    protected companion object {
        const val DURATION_KEY = "DURATION_KEY"
    }

    override fun equals(other: Any?): Boolean {
        return (this.uri == (other as? SongWrapper)?.uri)
    }

    override fun hashCode(): Int {
        var result = uri.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + duration.hashCode()
        return result
    }

    override fun MediaItem.toSong(): Song? {
        return this.toSongWrapper()
    }

    /**
     * Adds [SongWrapper] fields to [MediaItem] metadata.
     *
     * [title] and [artist] are put in [MediaItem.mediaMetadata] respective designated fields.
     * [duration] is put in extras by [DURATION_KEY]
     */
    override fun toMediaItem(): MediaItem {
        val baseMediaItem = super.toMediaItem()
        val baseMetadata = baseMediaItem.mediaMetadata
        val baseExtras = baseMetadata.extras ?: bundleOf()
        return baseMediaItem.buildUpon()
            .setMediaMetadata(
                baseMetadata.buildUpon()
                    .setTitle(title)
                    .setArtist(artist)
                    .setExtras(
                        Bundle().apply {
                            putAll(baseExtras)
                            putLong(DURATION_KEY, duration)
                        }
                    )
                    .build()
            ).build()
    }
}

fun MediaItem.toSongWrapper(): Song? {
    return when (val songType = this.mediaMetadata.extras?.getInt(Song.SONG_TYPE_KEY)) {
        SongType.LOCAL.ordinal -> LocalSong.fromMediaItem(this)
        SongType.REMOTE.ordinal -> RemoteSong.fromMediaItem(this)
        else -> throw IllegalArgumentException("Unknown song type $songType")
    }
}
