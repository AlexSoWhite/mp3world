package com.nafanya.mp3world.features.songListViews.actionDialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.databinding.DialogLocalSongActionBinding

enum class LocalSongAction {
    ADD_TO_FAVORITE,
    REMOVE_FROM_FAVORITE,
    GO_TO_ALBUM,
    GO_TO_ARTIST,
    GO_TO_EDIT_METADATA
}

class LocalSongActionDialog(
    context: Context,
    private val song: LocalSong
) : Dialog(context, R.style.Dialog) {

    private var binding: DialogLocalSongActionBinding =
        DialogLocalSongActionBinding.inflate(LayoutInflater.from(context))

    private var actionListener: ((LocalSongAction) -> Unit)? = null

    init {
        this.setContentView(binding.root)
        binding.apply {
            goToAlbum.setOnClickListener {
                actionListener?.invoke(LocalSongAction.GO_TO_ALBUM)
                dismiss()
            }
            goToAlbum.description.text = context.getString(
                R.string.go_to_album,
                song.album
            )
            goToArtist.setOnClickListener {
                actionListener?.invoke(LocalSongAction.GO_TO_ARTIST)
                dismiss()
            }
            goToArtist.description.text = context.getString(
                R.string.go_to_artist,
                song.artist
            )
            goToEditMetadata.setOnClickListener {
                actionListener?.invoke(LocalSongAction.GO_TO_EDIT_METADATA)
                dismiss()
            }
            songDescription.text =
                context.getString(R.string.song_description, song.artist, song.title)
            songDescription.isSelected = true
            dismissLocalActionDialog.setOnClickListener {
                this@LocalSongActionDialog.dismiss()
            }
        }
    }

    fun setActionListener(callback: (LocalSongAction) -> Unit) {
        actionListener = callback
    }

    fun setIsFavorite(isFavorite: Boolean) = with(binding.favouriteAction) {
        if (isFavorite) {
            icon.setImageResource(R.drawable.icv_favorite_filled)
            description.text =
                context.getString(R.string.remove_from_favourites)
            setOnClickListener {
                actionListener?.invoke(LocalSongAction.REMOVE_FROM_FAVORITE)
                dismiss()
            }
        } else {
            icon.setImageResource(R.drawable.icv_favorite_border)
            description.text = context.getString(R.string.add_to_favourites)
            setOnClickListener {
                actionListener?.invoke(LocalSongAction.ADD_TO_FAVORITE)
                dismiss()
            }
        }
    }
}
