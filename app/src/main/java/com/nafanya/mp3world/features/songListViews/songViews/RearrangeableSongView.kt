package com.nafanya.mp3world.features.songListViews.songViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.view.isVisible
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.databinding.SongViewBinding
import com.nafanya.mp3world.features.songListViews.baseViews.SongView
import com.nafanya.mp3world.features.songListViews.baseViews.SongViewHolder

class RearrangeableSongView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : SongView(context, attributeSet, defStyle) {

    override val binding: SongViewBinding =
        SongViewBinding.inflate(LayoutInflater.from(context), this, true)

    override fun setSong(
        song: SongWrapper,
        onSongClickCallback: () -> Unit
    ) {
        super.setSong(song) { onSongClickCallback() }
        binding.source.setImageResource(R.drawable.disk)
        binding.action.setImageResource(R.drawable.action_more)
    }

    fun attachToScreen(
        onLongPressCallback: (SongWrapper) -> Unit,
    ) {
        binding.root.setOnLongClickListener {
            mSong?.let {
                onLongPressCallback(it)
            }
            true
        }
    }

    fun moveToDraggableState(
        onDeletePressCallback: (SongWrapper) -> Unit
    ) {
        binding.dragHandler.isVisible = true
        binding.action.setImageResource(R.drawable.delete)
        binding.action.setOnClickListener { mSong?.let { onDeletePressCallback(it) } }
    }

    fun moveFromDraggableState() {
        binding.dragHandler.isVisible = false
        binding.action.setImageResource(R.drawable.action_more)
        binding.action.setOnClickListener {
            Toast.makeText(
                context,
                "Action",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

class RearrangeableSongViewHolder(
    private val view: RearrangeableSongView,
) : SongViewHolder(view) {

    fun attachToScreen(onLongPressCallback: (SongWrapper) -> Unit) {
        view.attachToScreen {
            onLongPressCallback(it)
        }
    }
}
