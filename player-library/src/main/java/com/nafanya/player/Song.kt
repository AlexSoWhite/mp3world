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

    @CallSuper
    open fun toMediaItem(): MediaItem {
        val extras = Bundle()
        // uri for responding to media item change
        extras.putParcelable(PlayerListener.URI_KEY, this.uri)
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
