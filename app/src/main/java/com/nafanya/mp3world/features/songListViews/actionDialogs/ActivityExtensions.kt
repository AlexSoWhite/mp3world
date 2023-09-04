package com.nafanya.mp3world.features.songListViews.actionDialogs

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.nafanya.mp3world.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.features.favourites.FavouritesManagerProxy

/**
 * Creates default local song action dialog
 */
fun AppCompatActivity.defaultLocalSongActionDialog(
    favouritesManagerProxy: FavouritesManagerProxy
): (LocalSong) -> Unit {
    return { song ->
        val dialog = createDialog(song) {
            when (it) {
                LocalSongAction.ADD_TO_FAVORITE -> { favouritesManagerProxy.addFavourite(song) }
                LocalSongAction.REMOVE_FROM_FAVORITE -> { favouritesManagerProxy.deleteFavourite(song) }
                LocalSongAction.GO_TO_ALBUM -> navigateToAlbum(song)
                LocalSongAction.GO_TO_ARTIST -> navigateToArtist(song)
            }
        }
        favouritesManagerProxy.isSongInFavourites(song).observe(this) {
            dialog.setIsFavorite(it)
        }
        dialog.show()
    }
}

fun Activity.createDialog(
    song: LocalSong,
    onActionClickCallback: (LocalSongAction) -> Unit
): LocalSongActionDialog {
    val dialog = LocalSongActionDialog(this, song)
    dialog.setActionListener {
        onActionClickCallback(it)
    }
    return dialog
}

fun Activity.navigateToAlbum(song: LocalSong) {
    ActivityStarter
        .Builder()
        .with(this)
        .createIntentToImmutablePlaylistActivityFromAlbum(
            song.albumId,
            song.album
        )
        .build()
        .startActivity()
}

fun Activity.navigateToArtist(song: LocalSong) {
    ActivityStarter
        .Builder()
        .with(this)
        .createIntentToImmutablePlaylistActivityFromArtist(
            song.artistId,
            song.artist
        )
        .build()
        .startActivity()
}
