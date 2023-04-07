package com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.nafanya.mp3world.databinding.ButtonAddPlaylistBinding
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.AllPlaylistsItemViewHolder
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.AllPlaylistsListItemView

class AddPlaylistsButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : AllPlaylistsListItemView(context, attributeSet, defStyle) {

    private val binding: ButtonAddPlaylistBinding =
        ButtonAddPlaylistBinding.inflate(LayoutInflater.from(context), this, true)

    fun setOnClickCallback(onClickCallback: () -> Unit) {
        binding.root.setOnClickListener {
            onClickCallback()
        }
    }
}

class AddPlaylistButtonViewHolder(
    private val view: AddPlaylistsButton
) : AllPlaylistsItemViewHolder(view) {

    fun bind(onAddCallback: () -> Unit) {
        view.setOnClickCallback { onAddCallback() }
    }
}
