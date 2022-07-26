package com.nafanya.mp3world.features.favorites.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.player.Song
import com.nafanya.mp3world.features.favorites.FavouriteListManager
import com.nafanya.mp3world.features.localStorage.LocalStorageProvider
import javax.inject.Inject
import kotlinx.coroutines.launch

class FavouriteListViewModel @Inject constructor(
    var localStorageProvider: LocalStorageProvider
) : ViewModel() {

    fun addFavourite(song: Song) {
        viewModelScope.launch {
            FavouriteListManager.add(song)
            localStorageProvider.addFavourite(song)
        }
    }

    fun deleteFavourite(song: Song) {
        viewModelScope.launch {
            FavouriteListManager.delete(song)
            localStorageProvider.deleteFavourite(song)
        }
    }
}
