package com.nafanya.mp3world.core.utils.listUtils.searching

import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @param queryFilterCallback function that should say if element of type [T] will
 * be included to query result or not based on a string query.
 */
class QueryFilter<T>(
    private val queryFilterCallback: (T, String) -> Boolean = { _, _ -> true }
) {

    val query = MutableStateFlow("")

    fun filter(list: List<T>): List<T> {
        return list.filter {
            queryFilterCallback(it, query.value)
        }
    }
}

val songQueryFilterCallback = { songWrapper: SongWrapper, s: String ->
    songWrapper.title.contains(s, true) || songWrapper.artist.contains(s, true)
}
