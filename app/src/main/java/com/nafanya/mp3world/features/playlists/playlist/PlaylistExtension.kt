package com.nafanya.mp3world.features.playlists.playlist

import com.nafanya.player.Playlist

fun com.nafanya.player.Playlist.toStorageEntity(): PlaylistStorageEntity {
    val songIds = mutableListOf<Long>()
    songList.forEach {
        songIds.add(it.id)
    }
    return PlaylistStorageEntity(songIds, id, name)
}