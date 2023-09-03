package com.nafanya.mp3world.features.favorites

import androidx.lifecycle.LiveData
import com.nafanya.mp3world.core.wrappers.LocalSong

/**
 * Interface to obtain adding and removing favourites. TO implement  it you should use
 * [FavouritesManager]
 */
interface FavouritesManagerProxy {

    fun isSongInFavourites(song: LocalSong): LiveData<Boolean>

    fun addFavourite(song: LocalSong)

    fun deleteFavourite(song: LocalSong)
}
