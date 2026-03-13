package com.nafanya.mp3world.presentation.song_list_views.action_dialogs

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nafanya.mp3world.core.coroutines.collectLatestInScope
import com.nafanya.mp3world.core.wrappers.song.ArtistMetadata
import com.nafanya.mp3world.presentation.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.domain.favourites.FavouritesManager

/**
 * Creates default local song action dialog
 */
fun AppCompatActivity.bottomSheetLocalSongActionDialog(): (LocalSong) -> Unit {
    return { song ->
        val dialog = LocalSongBottomSheetDialog.newInstance(song)
        dialog.show(supportFragmentManager, null)
    }
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

fun Activity.navigateToArtist(artist: ArtistMetadata) {
    ActivityStarter
        .Builder()
        .with(this)
        .createIntentToImmutablePlaylistActivityFromArtist(
            artist.id,
            artist.name
        )
        .build()
        .startActivity()
}
