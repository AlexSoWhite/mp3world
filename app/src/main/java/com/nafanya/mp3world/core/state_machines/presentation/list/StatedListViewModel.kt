package com.nafanya.mp3world.core.state_machines.presentation.list

import com.nafanya.mp3world.core.utils.list_utils.recycler.BaseListItem
import com.nafanya.mp3world.core.state_machines.ListStateModel
import com.nafanya.mp3world.core.state_machines.presentation.Data
import com.nafanya.mp3world.core.state_machines.presentation.StatedViewModel

/**
 * Class that manages state of list
 */
abstract class StatedListViewModel<DU, LI : BaseListItem> : StatedViewModel<List<DU>>(
    model = ListStateModel()
) {

    override fun processData(data: Data<List<DU>>) {
        when {
            data is Data.Success && data.data.isEmpty() -> onEmpty()
            else -> super.processData(data)
        }
    }

    abstract fun asListItems(list: List<DU>): List<LI>

    open fun onEmpty() = (model as ListStateModel).empty()
}
