package com.nafanya.mp3world.data.local_storage

import com.nafanya.mp3world.data.local_storage.api.FavouritesRepository
import com.nafanya.mp3world.data.local_storage.api.UserPlaylistsRepository

interface LocalStorageRepository : UserPlaylistsRepository, FavouritesRepository