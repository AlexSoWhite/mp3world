package com.nafanya.mp3world.domain.favourites

import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import kotlinx.coroutines.flow.Flow

/**
 * Interface to obtain adding and removing favourites. To implement it you should use
 * [FavouritesProvider]
 */
interface FavouritesManager {

    fun isSongInFavourites(song: LocalSong): Flow<Boolean>

    fun addFavourite(song: LocalSong)

    fun deleteFavourite(song: LocalSong)
}
