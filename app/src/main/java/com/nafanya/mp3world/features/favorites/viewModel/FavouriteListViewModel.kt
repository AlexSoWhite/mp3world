package com.nafanya.mp3world.features.favorites.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.features.favorites.FavouriteListManager
import com.nafanya.mp3world.features.localStorage.LocalStorageProvider
import com.nafanya.player.Song
import javax.inject.Inject
import kotlinx.coroutines.launch

class FavouriteListViewModel @Inject constructor(
    private val localStorageProvider: LocalStorageProvider,
    private val favouriteListManager: FavouriteListManager
) : ViewModel() {

    val playlist = favouriteListManager.favorites

    fun addFavourite(song: Song) {
        viewModelScope.launch {
            favouriteListManager.add(song)
            localStorageProvider.addFavourite(song)
        }
    }

    fun deleteFavourite(song: Song) {
        viewModelScope.launch {
            favouriteListManager.delete(song)
            localStorageProvider.deleteFavourite(song)
        }
    }
}
