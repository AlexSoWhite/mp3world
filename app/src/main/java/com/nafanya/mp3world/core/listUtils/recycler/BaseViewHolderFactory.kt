package com.nafanya.mp3world.core.listUtils.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface BaseViewHolderFactory<VH : RecyclerView.ViewHolder> {
    fun create(viewType: Int, parent: ViewGroup): VH
}
