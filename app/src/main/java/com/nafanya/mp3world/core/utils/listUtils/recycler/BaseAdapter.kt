package com.nafanya.mp3world.core.utils.listUtils.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<LI : BaseListItem, VH : RecyclerView.ViewHolder>(
    callback: DiffUtil.ItemCallback<LI>
) : ListAdapter<LI, VH>(callback) {

    abstract val viewHolderFactory: BaseViewHolderFactory<VH>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return viewHolderFactory.create(viewType, parent)
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].itemType
    }

    override fun submitList(list: List<LI>?) {
        super.submitList(list?.toMutableList())
    }
}
