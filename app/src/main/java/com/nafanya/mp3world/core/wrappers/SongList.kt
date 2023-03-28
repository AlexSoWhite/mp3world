package com.nafanya.mp3world.core.wrappers

import com.nafanya.mp3world.core.coroutines.AppDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Class that helps with working with mutable song collection.
 * Designed to handle bitmap updates.
 * To start emit values, call [SongList.unlock]. Make sure to use this method
 * after all elements of the collection are added, otherwise you can get
 * [ConcurrentModificationException]
 */
class SongList<T : SongWrapper>(
    private val emittingScope: CoroutineScope = CoroutineScope(AppDispatchers.unconfined)
) {

    private var mCurrentList: MutableList<T>? = null

    /**
     * Since [com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel] can
     * try to collect song data after emitting, we make replay = 1
     */
    private val mSongList = MutableSharedFlow<List<T>?>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val listFlow: Flow<List<T>?>
        get() = mSongList

    private var isLocked = true

    fun setEmptyList() {
        mCurrentList = mutableListOf()
    }

    fun addOrUpdateSongWrapper(song: T) {
        if (mCurrentList == null) {
            mCurrentList = mutableListOf()
        }
        val idx = mCurrentList!!.indexOf(song)
        if (idx != -1) {
            mCurrentList!![idx] = song
        } else {
            mCurrentList!!.add(song)
        }
        if (!isLocked) {
            emittingScope.launch {
                mSongList.emit(mCurrentList)
            }
        }
    }

    fun unlock() {
        isLocked = false
        emittingScope.launch {
            mSongList.emit(mCurrentList)
        }
    }

    fun lock() {
        isLocked = true
    }
}
