package com.nafanya.mp3world.features.songListViews.actionDialogs

import android.app.Dialog
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.databinding.DialogLocalSongActionBinding
import com.nafanya.mp3world.features.favorites.viewModel.FavouriteListViewModel

class LocalSongActionDialog(
    private val activity: FragmentActivity,
    private val song: LocalSong,
    private val favouriteListViewModel: FavouriteListViewModel
) : Dialog(activity, R.style.Dialog) {

    private var binding: DialogLocalSongActionBinding =
        DialogLocalSongActionBinding.inflate(LayoutInflater.from(context))

    private val favouriteObserver = Observer<Boolean> {
        with(binding) {
            if (it) {
                favouriteAction.icon.setImageResource(R.drawable.favorite)
                favouriteAction.description.text =
                    context.getString(R.string.remove_from_favourites)
                favouriteAction.setOnClickListener {
                    favouriteListViewModel.deleteFavourite(song)
                }
            } else {
                favouriteAction.icon.setImageResource(R.drawable.favorite_border)
                favouriteAction.description.text = context.getString(R.string.add_to_favourites)
                favouriteAction.setOnClickListener {
                    favouriteListViewModel.addFavourite(song)
                }
            }
        }
    }

    init {
        this.setContentView(binding.root)
        manageFavourite()
        manageAlbums()
        manageArtists()
        with(binding) {
            songDescription.text =
                context.getString(R.string.song_description, song.artist, song.title)
            songDescription.isSelected = true
            dismissLocalActionDialog.setOnClickListener {
                this@LocalSongActionDialog.dismiss()
            }
        }
    }

    private fun manageFavourite() = binding.apply {
        favouriteListViewModel.isSongInFavourite(song).observe(
            activity,
            favouriteObserver
        )
    }

    private fun manageAlbums() = binding.apply {
        goToAlbum.apply {
            description.text =
                context.getString(R.string.go_to_album, song.album)
            setOnClickListener {
                ActivityStarter.Builder()
                    .with(context)
                    .createIntentToImmutablePlaylistActivityFromAlbum(song.albumId, song.album)
                    .build()
                    .startActivity()
            }
        }
    }

    private fun manageArtists() = binding.apply {
        goToArtist.apply {
            description.text =
                context.getString(R.string.go_to_artist, song.artist)
            setOnClickListener {
                ActivityStarter.Builder()
                    .with(context)
                    .createIntentToImmutablePlaylistActivityFromArtist(song.artistId, song.artist)
                    .build()
                    .startActivity()
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        favouriteListViewModel.isSongInFavourite(song).removeObserver(favouriteObserver)
    }
}
