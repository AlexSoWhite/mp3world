package com.nafanya.mp3world.core.listUtils.searching

import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.stateMachines.list.StatedListViewModel
import com.nafanya.mp3world.core.wrappers.SongWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

interface SearchableStated<DU> {

    val queryFilter: StatedQueryFilter<DU>

    fun search(query: String) {
        queryFilter.query.value = query
    }

    suspend fun StatedListViewModel<DU, *>.setDataSourceFiltered(
        dataSource: Flow<Data<List<DU>>>
    ) {
        val newSource = combine(
            dataSource,
            queryFilter.query
        ) { data, _ ->
            if (data is Data.Success) {
                Data.Success(queryFilter.filter(data.data))
            } else {
                data
            }
        }
        this.setDataSource(newSource)
    }
}

/**
 * @param queryFilterCallback function that should say if element of type [T] will
 * be included to query result or not based on string query.
 */
class StatedQueryFilter<T>(
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
