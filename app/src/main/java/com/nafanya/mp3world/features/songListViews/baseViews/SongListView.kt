package com.nafanya.mp3world.features.songListViews.baseViews

import android.content.Context
import android.util.AttributeSet
import com.nafanya.mp3world.core.utils.listUtils.recycler.commonUi.BaseListItemView
import com.nafanya.mp3world.core.utils.listUtils.recycler.commonUi.BaseViewHolder

abstract class SongListView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : BaseListItemView(context, attributeSet, defStyle)

abstract class SongListItemViewHolder(
    itemView: SongListView
) : BaseViewHolder(itemView)
