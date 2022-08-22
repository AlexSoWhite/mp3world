package com.nafanya.mp3world.features.songListViews.topButtonViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.nafanya.mp3world.databinding.ButtonConfirmChangesBinding
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder
import com.nafanya.mp3world.features.songListViews.baseViews.SongListView

class ConfirmChangesButtonView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : SongListView(context, attributeSet, defStyle) {

    private val binding = ButtonConfirmChangesBinding
        .inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    fun setOnClickCallback(callback: () -> Unit) {
        binding.root.setOnClickListener { callback() }
    }
}

class ConfirmChangesButtonViewHolder(
    private val view: ConfirmChangesButtonView
) : SongListItemViewHolder(view) {

    fun bind(onClickCallback: () -> Unit) {
        view.setOnClickCallback(onClickCallback)
    }
}
