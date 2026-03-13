package com.nafanya.mp3world.presentation.song_list_views.action_dialogs

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nafanya.mp3world.core.coroutines.collectLatestInScope
import com.nafanya.mp3world.presentation.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.domain.favourites.FavouritesManager

/**
 * Creates default local song action dialog
 */
fun AppCompatActivity.defaultLocalSongActionDialog(
    favouritesManager: FavouritesManager
): (LocalSong) -> Unit {
    return { song ->
        val dialog = createDialog(song) {
            when (it) {
                LocalSongAction.ADD_TO_FAVORITE -> { favouritesManager.addFavourite(song) }
                LocalSongAction.REMOVE_FROM_FAVORITE -> {
                    favouritesManager.deleteFavourite(song)
                }
                LocalSongAction.GO_TO_ALBUM -> navigateToAlbum(song)
                LocalSongAction.GO_TO_ARTIST -> navigateToArtist(song)
            }
        }
        favouritesManager.isSongInFavourites(song).collectLatestInScope(lifecycleScope) {
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
//    ActivityStarter
//        .Builder()
//        .with(this)
//        .createIntentToImmutablePlaylistActivityFromArtist(
//            song.artistIds,
//            song.artistString
//        )
//        .build()
//        .startActivity()
}
