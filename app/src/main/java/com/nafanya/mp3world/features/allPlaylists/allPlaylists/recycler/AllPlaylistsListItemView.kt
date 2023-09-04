package com.nafanya.mp3world.features.allPlaylists.allPlaylists.recycler

import android.content.Context
import android.util.AttributeSet
import com.nafanya.mp3world.core.utils.listUtils.recycler.commonUi.BaseListItemView
import com.nafanya.mp3world.core.utils.listUtils.recycler.commonUi.BaseViewHolder

abstract class AllPlaylistsListItemView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : BaseListItemView(context, attributeSet, defStyle)

abstract class AllPlaylistsItemViewHolder(
    view: AllPlaylistsListItemView
) : BaseViewHolder(view)
