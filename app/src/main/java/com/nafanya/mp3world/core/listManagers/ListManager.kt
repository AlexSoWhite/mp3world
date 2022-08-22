package com.nafanya.mp3world.core.listManagers

import androidx.lifecycle.LiveData
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

abstract class ListManager {

    protected val listManagerScope = CoroutineScope(SupervisorJob())

    abstract fun getPlaylistByContainerId(id: Long): LiveData<PlaylistWrapper?>
}
