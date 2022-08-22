package com.nafanya.mp3world.features.songListViews.actionDialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong
import com.nafanya.mp3world.databinding.DialogRemoteSongActionBinding
import com.nafanya.mp3world.features.downloading.DownloadViewModel
import com.nafanya.mp3world.features.downloading.ResultType

class RemoteSongActionDialog(
    context: Context,
    private val song: RemoteSong,
    private val downloadViewModel: DownloadViewModel
) : Dialog(context, R.style.Dialog) {

    private val binding = DialogRemoteSongActionBinding.inflate(
        LayoutInflater.from(context)
    )

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
                downloadViewModel.download(song) {
                    if (it.type == ResultType.SUCCESS) {
                        Toast.makeText(
                            context.applicationContext,
                            "${song.artist} - ${song.title} загружено",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context.applicationContext,
                            "ошибка",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    downloadViewModel.updateSongList(it)
                }
            }
            dismissRemoteActionDialog.setOnClickListener {
                this@RemoteSongActionDialog.dismiss()
            }
        }
    }
}
