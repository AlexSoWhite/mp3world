package com.nafanya.mp3world.features.song_list_views.top_button_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.nafanya.mp3world.databinding.ButtonModifyPlaylistBinding
import com.nafanya.mp3world.features.song_list_views.base_views.SongListItemViewHolder
import com.nafanya.mp3world.features.song_list_views.base_views.SongListView

class ModifyPlaylistButtonView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : SongListView(context, attributeSet, defStyle) {

    private val binding =
        ButtonModifyPlaylistBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    fun setOnClickCallback(onClickCallback: () -> Unit) {
        binding.modifyPlaylistButton.setOnClickListener {
            onClickCallback()
        }
    }
}

class ModifyPlaylistButtonViewHolder(
    private val view: ModifyPlaylistButtonView
) : SongListItemViewHolder(view) {

    fun bind(onClickCallback: () -> Unit) {
        view.setOnClickCallback(onClickCallback)
    }
}
