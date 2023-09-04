package com.nafanya.mp3world.features.favourites

import androidx.lifecycle.LiveData
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong

/**
 * Interface to obtain adding and removing favourites. TO implement  it you should use
 * [FavouritesManager]
 */
interface FavouritesManagerProxy {

    fun isSongInFavourites(song: LocalSong): LiveData<Boolean>

    fun addFavourite(song: LocalSong)

    fun deleteFavourite(song: LocalSong)
}
