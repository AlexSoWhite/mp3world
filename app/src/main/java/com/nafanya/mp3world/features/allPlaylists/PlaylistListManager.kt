package com.nafanya.mp3world.features.allPlaylists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.launch

/**
 * Object that holds favourites data. Managed by DataBaseHolder and LocalStorageProvider.
 */
@Singleton
class PlaylistListManager @Inject constructor(
    private val interactor: AllPlaylistsLocalStorageInteractor,
    private val ioCoroutineProvider: IOCoroutineProvider
) : ListManager() {

    private val mPlaylists = MutableLiveData<List<PlaylistWrapper>>()
    val playlists: LiveData<List<PlaylistWrapper>>
        get() = mPlaylists

    init {
        interactor.allPlaylists.observeForever {
            mPlaylists.postValue(it)
        }
        listManagerScope.launch {
            interactor.getAll()
        }
    }

    override fun getPlaylistByContainerId(id: Long): LiveData<PlaylistWrapper?> {
        return playlists.map {
            it.firstOrNull { playlist -> playlist.id == id }
        }
    }

    fun addPlaylist(playlistName: String) {
        val playlist = PlaylistWrapper(
            name = playlistName,
            id = playlists.value?.maxOfOrNull { it.id + 1 } ?: 0,
            songList = listOf(),
            position = playlists.value?.maxOfOrNull { it.position + 1 } ?: 0
        )
        ioCoroutineProvider.ioScope.launch {
            interactor.insert(playlist)
        }
    }

    fun updatePlaylist(playlist: PlaylistWrapper) {
        val temp = mutableListOf<PlaylistWrapper>()
        playlists.value?.forEach {
            temp.add(it)
        }
        playlist.songList.forEach {
            playlist.imageSource = it
        }
        val index = temp.indexOf(playlist)
        if (index != -1) {
            ioCoroutineProvider.ioScope.launch {
                interactor.update(temp[index], playlist)
            }
        }
    }

    fun deletePlaylist(playlist: PlaylistWrapper) {
        val temp = playlists.value as MutableList<PlaylistWrapper>
        temp.remove(playlist)
        ioCoroutineProvider.ioScope.launch {
            interactor.delete(playlist)
        }
    }
}
