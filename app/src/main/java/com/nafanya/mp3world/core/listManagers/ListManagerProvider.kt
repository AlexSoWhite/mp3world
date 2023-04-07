package com.nafanya.mp3world.core.listManagers

import dagger.MapKey
import javax.inject.Inject

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ListManagerKey(val value: Int)
const val ALBUM_LIST_MANAGER_KEY = 0
const val ARTIST_LIST_MANAGER_KEY = 1
const val FAVOURITE_LIST_MANAGER_KEY = 2
const val PLAYLIST_LIST_MANAGER_KEY = 3
const val ALL_SONGS_LIST_MANAGER_KEY = 4

class ListManagerProvider @Inject constructor(
    private val providerMap: Map<Int, @JvmSuppressWildcards ListManager>
) {

    @Suppress("UNCHECKED_CAST")
    fun getListManager(key: Int): ListManager {
        providerMap[key]?.let {
            return it
        }
        throw IllegalArgumentException("No such provider for $key")
    }
}
