package com.nafanya.mp3world.features.remoteSongs.songSearchers

import dagger.MapKey
import javax.inject.Inject

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class SongSearcherKey(val value: Int)
const val MUSMORE = 0
const val HITMO_TOP = 1

class SongSearchersProvider @Inject constructor(
    private val songSearchersMap: Map<Int, @JvmSuppressWildcards SongSearcher>
) {

    fun getSongSearcher(key: Int): SongSearcher {
        songSearchersMap[key]?.let {
            return it
        }
        throw IllegalArgumentException("No such provider for $key")
    }

    fun getAll(): List<SongSearcher> {
        return songSearchersMap.values.toList()
    }
}
