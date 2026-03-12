package com.nafanya.player.aoede_player

import androidx.media3.common.MediaItem
import com.nafanya.player.Song

interface MediaItemConverter {
    fun getSongFromMediaItem(mediaItem: MediaItem): Song?
}
