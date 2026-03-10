package com.nafanya.mp3world.features.user_playlists.presentation.view_playlists.recycler

import android.content.Context
import android.util.AttributeSet
import com.nafanya.mp3world.core.utils.list_utils.recycler.view.BaseListItemView
import com.nafanya.mp3world.core.utils.list_utils.recycler.view.BaseViewHolder

abstract class AllPlaylistsListItemView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : BaseListItemView(context, attributeSet, defStyle)

abstract class AllPlaylistsItemViewHolder(
    view: AllPlaylistsListItemView
) : BaseViewHolder(view)
