package com.nafanya.mp3world.core.utils.listUtils.searching

import com.nafanya.mp3world.core.stateMachines.commonUi.Data
import com.nafanya.mp3world.core.stateMachines.commonUi.list.StatedListViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Class that an [Searchable] viewModel should use to obtain searching functionality
 */
class SearchProcessor<DU>(private val queryFilter: QueryFilter<DU>) {

    /**
     * Sets data source for [viewModel] by combining [dataSource] with [queryFilter]
     */
    fun setup(
        viewModel: StatedListViewModel<DU, *>,
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
        viewModel.setDataSource(newSource)
    }

    /**
     * Applies filter to [Searchable] viewModel data source based on [query]
     */
    fun search(query: String) {
        queryFilter.query.value = query
    }
}
