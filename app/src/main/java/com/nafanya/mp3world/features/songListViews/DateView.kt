package com.nafanya.mp3world.features.songListViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.nafanya.mp3world.databinding.DateViewBinding
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder
import com.nafanya.mp3world.features.songListViews.baseViews.SongListView

class DateView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : SongListView(context, attributeSet, defStyle) {

    private val binding = DateViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setDate(date: String) {
        binding.dateText.text = date
    }
}

class DateViewHolder(
    private val dateView: DateView
) : SongListItemViewHolder(dateView) {

    fun bind(date: String) {
        dateView.setDate(date)
    }
}
