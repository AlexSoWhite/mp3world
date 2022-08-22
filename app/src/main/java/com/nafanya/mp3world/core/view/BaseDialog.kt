package com.nafanya.mp3world.core.view

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.nafanya.mp3world.R

class BaseDialog(
    context: Context,
    @LayoutRes layoutRes: Int
) {

    val dialog: Dialog = Dialog(context, R.style.Dialog)
    val view: View = LayoutInflater.from(context).inflate(layoutRes, null)

    init {
        dialog.setContentView(view)
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
