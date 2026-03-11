package com.nafanya.mp3world.core.state_machines.presentation.list

import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.nafanya.mp3world.core.utils.list_utils.recycler.BaseAdapter
import com.nafanya.mp3world.core.utils.list_utils.recycler.BaseListItem
import com.nafanya.mp3world.core.utils.list_utils.recycler.view.BaseViewHolder
import com.nafanya.mp3world.core.state_machines.LState
import com.nafanya.mp3world.core.state_machines.State
import com.nafanya.mp3world.core.state_machines.presentation.StatedFragment
import com.nafanya.mp3world.core.state_machines.presentation.StatedViewModel

/**
 * [StatedFragment] with additional functionality to render [LState.Empty]. Submits data to recycler
 * when necessary.
 */
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
