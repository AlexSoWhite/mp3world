package com.nafanya.mp3world.core.utils.listUtils.recycler.commonUi

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListItemView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}

abstract class BaseViewHolder(
    view: BaseListItemView
) : RecyclerView.ViewHolder(view)
