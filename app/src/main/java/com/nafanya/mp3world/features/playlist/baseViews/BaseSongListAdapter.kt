package com.nafanya.mp3world.features.playlist.baseViews

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.listUtils.recycler.BaseAdapter
import com.nafanya.mp3world.features.songListViews.ListItemType
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.mp3world.features.songListViews.SongListItemDiffUtilCallback
import com.nafanya.mp3world.features.songListViews.SongListItemViewHolderFactory
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder

abstract class BaseSongListAdapter :
    BaseAdapter<SongListItem, SongListItemViewHolder>(SongListItemDiffUtilCallback) {

    private val mOnBound = MutableLiveData(false)
    val onBound: LiveData<Boolean>
        get() = mOnBound

    override val viewHolderFactory: SongListItemViewHolderFactory by lazy {
        SongListItemViewHolderFactory()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        @ListItemType viewType: Int
    ): SongListItemViewHolder {
        return viewHolderFactory.create(viewType, parent)
    }

    @CallSuper
    override fun onBindViewHolder(holder: SongListItemViewHolder, position: Int) {
        mOnBound.value = true
    }

    @ListItemType
    override fun getItemViewType(position: Int): Int {
        return currentList[position].itemType
    }
}
