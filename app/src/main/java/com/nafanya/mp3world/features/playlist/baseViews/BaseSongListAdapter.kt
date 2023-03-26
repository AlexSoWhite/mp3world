package com.nafanya.mp3world.features.playlist.baseViews

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.listUtils.recycler.BaseAdapter
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.songListViews.ListItemType
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.mp3world.features.songListViews.SongListItemDiffUtilCallback
import com.nafanya.mp3world.features.songListViews.SongListItemViewHolderFactory
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder
import com.nafanya.mp3world.features.songListViews.baseViews.SongView
import com.nafanya.mp3world.features.songListViews.baseViews.SongViewHolder

abstract class BaseSongListAdapter :
    BaseAdapter<SongListItem, SongListItemViewHolder>(SongListItemDiffUtilCallback) {

    private var mCurrentPlayingSong: SongWrapper? = null

    private val mCurrentSelectedView = MutableLiveData<SongView>()
    val currentSelectedView: LiveData<SongView>
        get() = mCurrentSelectedView

    override val viewHolderFactory: SongListItemViewHolderFactory by lazy {
        SongListItemViewHolderFactory()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        @ListItemType viewType: Int
    ): SongListItemViewHolder {
        return viewHolderFactory.create(viewType, parent)
    }

    fun setCurrentPlayingSong(song: SongWrapper) {
        mCurrentPlayingSong = song
    }

    @CallSuper
    override fun onBindViewHolder(holder: SongListItemViewHolder, position: Int) {
        if (holder is SongViewHolder) {
            val songView = holder.itemView as SongView
            songView.setOnSongSelectedListener {
                mCurrentSelectedView.value = it
            }
            mCurrentPlayingSong?.let {
                holder.updateIsPlaying(it)
            }
        }
    }

    @ListItemType
    override fun getItemViewType(position: Int): Int {
        return currentList[position].itemType
    }
}
