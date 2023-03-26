package com.nafanya.mp3world.core.stateMachines.list

import com.nafanya.mp3world.core.listUtils.recycler.BaseListItem
import com.nafanya.mp3world.core.stateMachines.ListStateModel
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.stateMachines.common.StatedViewModel

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
