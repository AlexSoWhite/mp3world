package com.nafanya.mp3world.features.favorites.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.features.favorites.FavouriteListManager
import com.nafanya.mp3world.features.localStorage.LocalStorageProvider
import com.nafanya.mp3world.core.domain.Song
import kotlinx.coroutines.launch
import javax.inject.Inject

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
