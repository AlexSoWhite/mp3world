package com.nafanya.mp3world.features.songListViews.songViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.utils.dpToPx
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.databinding.SongViewBinding
import com.nafanya.mp3world.features.songListViews.baseViews.SongView
import com.nafanya.mp3world.features.songListViews.baseViews.SongViewHolder

class AddableRemovableSongView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : SongView(context, attributeSet, defStyle) {

    companion object {
        const val DURATION_PADDING_END = 25
    }

    override val binding: SongViewBinding =
        SongViewBinding.inflate(LayoutInflater.from(context), this, true)

    private lateinit var playlist: PlaylistWrapper

    private lateinit var onAddCallback: (SongWrapper) -> Unit
    private lateinit var onRemoveCallback: (SongWrapper) -> Unit

    init {
        binding.duration.setPadding(
            binding.duration.paddingLeft,
            binding.duration.paddingTop,
            DURATION_PADDING_END.dpToPx(),
            binding.duration.paddingTop
        )
    }

    override fun setSong(song: SongWrapper, onSongClickCallback: () -> Unit) {
        super.setSong(song) { onSongClickCallback() }
        binding.source.setImageResource(R.drawable.disk)
    }

    fun attachToPlaylist(
        playlist: PlaylistWrapper,
        onAddCallback: (SongWrapper) -> Unit,
        onRemoveCallback: (SongWrapper) -> Unit
    ) {
        this.playlist = playlist
        this.onAddCallback = onAddCallback
        this.onRemoveCallback = onRemoveCallback
        if (this.playlist.songList.contains(mSong)) {
            binding.action.setImageResource(R.drawable.delete)
            binding.action.setOnClickListener { removeFromPlaylist() }
        } else {
            binding.action.setImageResource(R.drawable.add)
            binding.action.setOnClickListener { addToPlaylist() }
        }
    }

    private fun removeFromPlaylist() {
        onRemoveCallback(mSong)
        binding.action.setImageResource(R.drawable.add)
        binding.action.setOnClickListener {
            addToPlaylist()
        }
    }

    private fun addToPlaylist() {
        onAddCallback(mSong)
        binding.action.setImageResource(R.drawable.delete)
        binding.action.setOnClickListener {
            removeFromPlaylist()
        }
    }
}

class AddableRemovableSongViewHolder(
    private val view: AddableRemovableSongView
) : SongViewHolder(view) {

    fun attachToPlaylist(
        playlist: PlaylistWrapper,
        onAddCallback: (SongWrapper) -> Unit,
        onRemoveCallback: (SongWrapper) -> Unit
    ) {
        view.attachToPlaylist(playlist, onAddCallback, onRemoveCallback)
    }
}
