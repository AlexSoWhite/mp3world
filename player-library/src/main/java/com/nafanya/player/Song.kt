package com.nafanya.player

import android.net.Uri
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.nafanya.player.aoede_player.PlayerListener

/**
 * @param uri [Uri] for [AoedePlayer]
 */
@Suppress("UnnecessaryAbstractClass")
abstract class Song(open val uri: Uri) {

    companion object {
        const val SONG_TYPE_KEY = "SONG_TYPE_KEY"
        const val URI_KEY = "uri"
    }

    abstract fun getSongType(): Int

    abstract fun MediaItem.toSong(): Song?

    /**
     * Puts [Song] fields to [MediaItem] metadata. Puts [getSongType] result there as well.
     *
     * [uri] filed is put in extras by [URI_KEY]. Additionally [uri] is submitted via designated method for player.
     * [getSongType] result is put in extras by [SONG_TYPE_KEY]
     */
    @CallSuper
    open fun toMediaItem(): MediaItem {
        val extras = Bundle()
        // uri for responding to media item change
        extras.putParcelable(URI_KEY, this.uri)
        extras.putInt(SONG_TYPE_KEY, getSongType())
        return MediaItem.Builder()
            .setUri(uri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setExtras(extras)
                    .build()
            )
            .build()
    }
}
