package com.nafanya.mp3world.core.wrappers.song

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.player.Song
import kotlinx.parcelize.Parcelize

// for media item extras
enum class SongType {
    LOCAL,
    REMOTE
}

@Parcelize
data class ArtistMetadata(
    val id: Long,
    val name: String
) : Parcelable

abstract class SongWrapper(
    uri: Uri,
    open val title: String,
    open val artists: List<ArtistMetadata>,
    open val duration: Long
) : Song(uri) {

    protected companion object {
        const val DURATION_KEY = "DURATION_KEY"
        const val ARTIST_ARRAY_KEY = "ARTIST_ARRAY_KEY"
    }

    override fun equals(other: Any?): Boolean {
        return (this.uri == (other as? SongWrapper)?.uri)
    }

    override fun hashCode(): Int {
        var result = uri.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artists.hashCode()
        result = 31 * result + duration.hashCode()
        return result
    }

    override fun MediaItem.toSong(): Song? {
        return this.toSongWrapper()
    }

    /**
     * Adds [SongWrapper] fields to [MediaItem] metadata.
     *
     * [title] and combined [artists] are put in [MediaItem.mediaMetadata] respective designated fields.
     * [duration] and [artists] data are put in extras by [DURATION_KEY]
     */
    override fun toMediaItem(): MediaItem {
        val baseMediaItem = super.toMediaItem()
        val baseMetadata = baseMediaItem.mediaMetadata
        val baseExtras = baseMetadata.extras ?: bundleOf()
        return baseMediaItem.buildUpon()
            .setMediaMetadata(
                baseMetadata.buildUpon()
                    .setTitle(title)
                    .setArtist(artists.joinArtists())
                    .setExtras(
                        Bundle().apply {
                            putAll(baseExtras)
                            putLong(DURATION_KEY, duration)
                            putParcelableArray(ARTIST_ARRAY_KEY, artists.toTypedArray())
                        }
                    )
                    .build()
            ).build()
    }
}

fun String.splitArtistNames(): List<String> {
    return split(",", "/").map { it.trim() }
}

fun List<String>.joinArtistsNames(): String {
    return joinToString(", ")
}

fun List<ArtistMetadata>.joinArtists(): String {
    return map { it.name }.joinArtistsNames()
}

fun MediaItem.toSongWrapper(): Song? {
    return when (val songType = this.mediaMetadata.extras?.getInt(Song.SONG_TYPE_KEY)) {
        SongType.LOCAL.ordinal -> LocalSong.fromMediaItem(this)
        SongType.REMOTE.ordinal -> RemoteSong.fromMediaItem(this)
        else -> throw IllegalArgumentException("Unknown song type $songType")
    }
}
