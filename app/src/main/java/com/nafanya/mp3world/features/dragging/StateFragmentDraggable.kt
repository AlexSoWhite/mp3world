package com.nafanya.mp3world.features.dragging

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.core.listUtils.StateFragment
import com.nafanya.mp3world.core.listUtils.StateMachine
import com.nafanya.mp3world.core.listUtils.recycler.BaseListItem

abstract class StateFragmentDraggable<DU, LI : BaseListItem> :
    StateFragment<DU, LI>() {

    abstract val draggableStateMachine: StateMachineDraggable<DU, LI>
    final override val stateMachine: StateMachine<DU, LI>
        get() = draggableStateMachine

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemTouchHelper = ItemTouchHelper(touchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recycler)
        draggableStateMachine.inDraggableMode.observe(viewLifecycleOwner) {
            if (it) {
                moveToDraggableState()
            } else {
                moveFromDraggableState()
            }
        }
    }

    /**
     * Method which has to notify adapter that items are moved and do (optional)
     * related work
     */
    abstract fun onMoved(startPosition: Int, endPosition: Int)

    abstract fun moveToDraggableState()

    abstract fun moveFromDraggableState()

    protected open fun getMovementFlagsOverride(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return -1
    }

    private val touchCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
        0
    ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val startPosition = viewHolder.bindingAdapterPosition
            val endPosition = target.bindingAdapterPosition

            onMoved(startPosition, endPosition)
            return true
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val overrodeFlags = getMovementFlagsOverride(recyclerView, viewHolder)
            return if (overrodeFlags > -1) {
                overrodeFlags
            } else {
                super.getMovementFlags(recyclerView, viewHolder)
            }
        }

        @Suppress("EmptyFunctionBlock")
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { }
    }
}
