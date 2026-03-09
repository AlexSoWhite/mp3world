package com.nafanya.mp3world.features.userPlaylists.data

import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper

// todo: all this package should be inside localStorage package
fun PlaylistWrapper.toStorageEntity(): Pair<PlaylistStorageEntity, List<PlaylistSongsEntity>> {
    val playlistEntity = PlaylistStorageEntity(
        id = this.id,
        name = this.name,
        position = this.position
    )
    val list = mutableListOf<PlaylistSongsEntity>()
    list.addAll(
        this.songList.mapIndexed { index, songWrapper ->
            PlaylistSongsEntity(
                playlistId = this.id,
                uri = songWrapper.uri.toString(),
                position = index
            )
        }
    )
    return Pair(playlistEntity, list)
}
