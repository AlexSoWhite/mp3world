package com.nafanya.mp3world.features.song_list_views.base_views

import android.content.Context
import android.util.AttributeSet
import com.nafanya.mp3world.core.utils.list_utils.recycler.view.BaseListItemView
import com.nafanya.mp3world.core.utils.list_utils.recycler.view.BaseViewHolder

abstract class SongListView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : BaseListItemView(context, attributeSet, defStyle)

abstract class SongListItemViewHolder(
    itemView: SongListView
) : BaseViewHolder(itemView)
