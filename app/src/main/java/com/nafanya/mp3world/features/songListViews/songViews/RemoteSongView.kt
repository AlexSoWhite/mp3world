package com.nafanya.mp3world.features.songListViews.songViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.databinding.SongViewBinding
import com.nafanya.mp3world.features.songListViews.baseViews.SongView
import com.nafanya.mp3world.features.songListViews.baseViews.SongViewHolder

class RemoteSongView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : SongView(context, attributeSet, defStyle) {

    override val binding: SongViewBinding =
        SongViewBinding.inflate(LayoutInflater.from(context), this, true)

    override fun setSong(song: SongWrapper, onSongClickCallback: () -> Unit) {
        super.setSong(song) { onSongClickCallback() }
        binding.source.setImageResource(R.drawable.icv_web)
        binding.action.setImageResource(R.drawable.icv_download)
    }
}

class RemoteSongViewHolder(
    view: RemoteSongView
) : SongViewHolder(view)
