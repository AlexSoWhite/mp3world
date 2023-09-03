package com.nafanya.mp3world.features.allPlaylists.modifyPlaylist

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.DialogDiscardConfirmationBinding

class DiscardChangesDialog(
    context: Context,
    onDiscardCallback: () -> Unit
) : Dialog(context, R.style.Dialog) {

    private val binding = DialogDiscardConfirmationBinding.inflate(
        LayoutInflater.from(context)
    )

    init {
        setContentView(binding.root)
        binding.discardChangesCancel.setOnClickListener {
            this.dismiss()
        }
        binding.discard.setOnClickListener {
            onDiscardCallback()
            this.dismiss()
        }
    }
}
