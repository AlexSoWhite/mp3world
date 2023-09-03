package com.nafanya.mp3world.features.allPlaylists.allPlaylists

import android.app.Dialog
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.view.BaseDialog

fun AppCompatActivity.createAddPlaylistDialog(
    onSubmitCallback: (String) -> Unit
): Dialog {
    val builder = BaseDialog(this, R.layout.dialog_add_playlist)
    builder.apply {
        val input = view.findViewById<TextInputEditText>(R.id.input)
        input.setOnKeyListener(
            View.OnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    val text = input.text?.toString()
                    submitCreation(
                        this@createAddPlaylistDialog,
                        text,
                        dialog,
                        onSubmitCallback
                    )
                    return@OnKeyListener true
                }
                false
            }
        )
        view.findViewById<Button>(R.id.playlist_add_cancel).setOnClickListener {
            dialog.dismiss()
        }
        view.findViewById<Button>(R.id.create).setOnClickListener {
            submitCreation(
                this@createAddPlaylistDialog,
                input.text?.toString(),
                dialog,
                onSubmitCallback
            )
        }
        return dialog
    }
}

private fun submitCreation(
    activity: AppCompatActivity,
    text: String?,
    dialog: Dialog,
    onSubmitCallback: (String) -> Unit
) {
    if (text.isNullOrEmpty() || text.isBlank()) {
        Toast.makeText(
            activity,
            "название плейлиста не может быть пустым",
            Toast.LENGTH_SHORT
        ).show()
    } else {
        onSubmitCallback(text)
        dialog.dismiss()
    }
}

fun AppCompatActivity.createDeletePlaylistDialog(
    playlistName: String,
    onDeleteCallback: () -> Unit
): Dialog {
    val builder = BaseDialog(this, R.layout.dialog_delete_playlist)
    builder.apply {
        view.findViewById<TextView>(R.id.message).text =
            this@createDeletePlaylistDialog.getString(
                R.string.delete_playlist_message,
                playlistName
            )
        view.findViewById<Button>(R.id.discard_changes_cancel).setOnClickListener {
            dialog.dismiss()
        }
        view.findViewById<Button>(R.id.delete).setOnClickListener {
            onDeleteCallback()
            dialog.dismiss()
        }
        return dialog
    }
}
