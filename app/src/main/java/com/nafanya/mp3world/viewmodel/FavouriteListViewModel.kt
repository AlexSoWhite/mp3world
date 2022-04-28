package com.nafanya.mp3world.viewmodel

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.model.listManagers.FavouriteListManager
import com.nafanya.mp3world.model.localStorage.LocalStorageProvider
import com.nafanya.mp3world.model.wrappers.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavouriteListViewModel @Inject constructor(
    var localStorageProvider: LocalStorageProvider
): ViewModel() {

    fun addFavourite(song: Song) {
        FavouriteListManager.add(song)
        localStorageProvider.addFavourite(song)
    }

    fun deleteFavourite(song: Song) {
        FavouriteListManager.delete(song)
        localStorageProvider.deleteFavourite(song)
    }
}
