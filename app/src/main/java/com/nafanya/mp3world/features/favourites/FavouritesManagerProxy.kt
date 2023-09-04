package com.nafanya.mp3world.features.favourites

import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import kotlinx.coroutines.flow.Flow

/**
 * Interface to obtain adding and removing favourites. TO implement  it you should use
 * [FavouritesManager]
 */
interface FavouritesManagerProxy {

    fun isSongInFavourites(song: LocalSong): Flow<Boolean>

    fun addFavourite(song: LocalSong)

    fun deleteFavourite(song: LocalSong)
}
