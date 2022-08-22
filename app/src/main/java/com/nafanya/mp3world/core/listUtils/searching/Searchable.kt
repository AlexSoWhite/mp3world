package com.nafanya.mp3world.core.listUtils.searching

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.listUtils.StateMachine
import com.nafanya.mp3world.core.wrappers.SongWrapper

/**
 * Interface that allows to set up a filter for [StateMachine] to search string queries.
 * To use searching functionality [applyFilter] method must be called.
 */
interface Searchable<DU> {

    /**
     * [QueryFilter] object that holds a query filter callback.
     */
    val filter: QueryFilter<DU>

    /**
     * Method that updates current query.
     */
    fun search(query: String) {
        filter.query.value = query
    }

    /**
     * Method that should be called inside [StateMachine] to apply [QueryFilter]
     * to it.
     */
    fun applyFilter(stateMachine: StateMachine<DU, *>) = stateMachine.apply {
        resetDataSourceObserver {
            it?.let { list ->
                stateMachine.data.value = filter.filter(list)
            }
        }
        compositeObservable.addObserver(filter.query) {
            dataSource.value?.let {
                data.value = filter.filter(it)
            }
        }
        addStateUpdateTrigger(filter.query)
    }
}

/**
 * @param queryFilterCallback function that should say if element of type [T] will
 * be included to query result or not based on string query.
 */
class QueryFilter<T>(
    private val queryFilterCallback: (T, String) -> Boolean = { _, _ -> true }
) {

    val query = MutableLiveData("")

    fun filter(list: List<T>): List<T> {
        return list.filter {
            queryFilterCallback(it, query.value ?: "")
        }
    }
}

val SongQueryFilter = QueryFilter<SongWrapper> { songWrapper, s ->
    songWrapper.title.contains(s, true) || songWrapper.artist.contains(s, true)
}
