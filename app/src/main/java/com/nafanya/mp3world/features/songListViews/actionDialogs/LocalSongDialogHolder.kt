package com.nafanya.mp3world.features.songListViews.actionDialogs

import android.content.Context
import com.nafanya.mp3world.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.features.favorites.viewModel.FavouriteListViewModel

interface LocalSongDialogHolder {

    val actualFavouriteListViewModel: FavouriteListViewModel

    fun createDialog(context: Context, song: LocalSong): LocalSongActionDialog {
        val dialog = LocalSongActionDialog(context, song)
        dialog.setActionListener {
            onActionClickCallback(context, song, it)
        }
        return dialog
    }

    fun onActionClickCallback(context: Context, song: LocalSong, action: LocalSongAction) {
        when (action) {
            LocalSongAction.ADD_TO_FAVORITE -> actualFavouriteListViewModel.addFavourite(song)
            LocalSongAction.REMOVE_FROM_FAVORITE ->
                actualFavouriteListViewModel.deleteFavourite(song)
            LocalSongAction.GO_TO_ALBUM -> {
                ActivityStarter
                    .Builder()
                    .with(context)
                    .createIntentToImmutablePlaylistActivityFromAlbum(
                        song.albumId,
                        song.album
                    )
                    .build()
                    .startActivity()
            }
            LocalSongAction.GO_TO_ARTIST -> {
                ActivityStarter
                    .Builder()
                    .with(context)
                    .createIntentToImmutablePlaylistActivityFromArtist(
                        song.artistId,
                        song.artist
                    )
                    .build()
                    .startActivity()
            }
        }
    }
}
