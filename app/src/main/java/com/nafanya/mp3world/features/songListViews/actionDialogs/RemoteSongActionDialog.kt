package com.nafanya.mp3world.features.songListViews.actionDialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong
import com.nafanya.mp3world.databinding.DialogRemoteSongActionBinding

class RemoteSongActionDialog(
    context: Context,
    private val song: RemoteSong
) : Dialog(context, R.style.Dialog) {

    companion object {
        const val DOWNLOAD = 0
    }

    private val binding = DialogRemoteSongActionBinding.inflate(
        LayoutInflater.from(context)
    )

    private var onActionClickedCallback: ((Int) -> Unit)? = null

    init {
        setContentView(binding.root)
        with(binding) {
            songDescription.text =
                context.getString(
                    R.string.song_description,
                    song.artist,
                    song.title
                )
            songDescription.isSelected = true
            downloadAction.description.text =
                context.getString(
                    R.string.download_song,
                    song.artist,
                    song.title
                )
            downloadAction.setOnClickListener {
                onActionClickedCallback?.invoke(DOWNLOAD)
                dismiss()
            }
            dismissRemoteActionDialog.setOnClickListener {
                dismiss()
            }
        }
    }

    fun setOnActionClickedListener(callback: (Int) -> Unit) {
        onActionClickedCallback = callback
    }
}
