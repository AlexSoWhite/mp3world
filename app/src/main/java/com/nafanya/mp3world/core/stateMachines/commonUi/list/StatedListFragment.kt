package com.nafanya.mp3world.core.stateMachines.commonUi.list

import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.nafanya.mp3world.core.listUtils.recycler.BaseAdapter
import com.nafanya.mp3world.core.listUtils.recycler.BaseListItem
import com.nafanya.mp3world.core.listUtils.recycler.views.BaseViewHolder
import com.nafanya.mp3world.core.stateMachines.LState
import com.nafanya.mp3world.core.stateMachines.State
import com.nafanya.mp3world.core.stateMachines.commonUi.StatedFragment
import com.nafanya.mp3world.core.stateMachines.commonUi.StatedViewModel

abstract class StatedListFragment<
    VB : ViewBinding,
    DU,
    LI : BaseListItem
    > : StatedFragment<VB, List<DU>>() {

    abstract val listViewModel: StatedListViewModel<DU, LI>
    final override val statedViewModel: StatedViewModel<List<DU>>
        get() = listViewModel

    abstract val adapter: BaseAdapter<LI, out BaseViewHolder>

    override fun renderState(state: State<List<DU>>) = when (state) {
        is LState.Empty -> renderEmpty()
        else -> super.renderState(state)
    }

    @CallSuper
    override fun renderSuccess(data: List<DU>) {
        adapter.submitList(listViewModel.asListItems(data))
    }

    @CallSuper
    open fun renderEmpty() {
        adapter.submitList(emptyList())
    }

    @CallSuper
    override fun renderUpdated(data: List<DU>) {
        adapter.submitList(listViewModel.asListItems(data))
    }
}
