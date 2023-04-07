package com.nafanya.mp3world.features.favorites.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.favorites.FavouriteListManager
import javax.inject.Inject
import kotlinx.coroutines.launch

class FavouriteListViewModel @Inject constructor(
    private val favouriteListManager: FavouriteListManager
) : ViewModel() {

    val playlist = favouriteListManager.favorites

    fun addFavourite(song: SongWrapper) {
        viewModelScope.launch {
            favouriteListManager.add(song)
        }
    }

    fun deleteFavourite(song: SongWrapper) {
        viewModelScope.launch {
            favouriteListManager.delete(song)
        }
    }

    fun isSongInFavourite(song: SongWrapper): LiveData<Boolean> {
        return playlist.map {
            it.songList.map { song ->
                song.uri
            }.contains(song.uri)
        }
    }
}
