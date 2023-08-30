package com.nafanya.mp3world.core.wrappers

import android.net.Uri

interface UriFactory {

    /**
     * Returns [Uri] for [com.nafanya.player.AoedePlayer] for local song with given [id]
     */
    fun getUri(id: Long): Uri

    /**
     * Returns [Uri] for [com.nafanya.player.AoedePlayer] for remote song with given [url]
     */
    fun getUri(url: String): Uri
}
