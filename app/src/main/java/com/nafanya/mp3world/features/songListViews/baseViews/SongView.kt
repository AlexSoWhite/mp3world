package com.nafanya.mp3world.features.songListViews.baseViews

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import com.nafanya.mp3world.core.utils.timeConverters.TimeConverter
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.databinding.SongViewBinding
import com.nafanya.player.Song

abstract class SongView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : SongListView(context, attributeSet, defStyle) {

    companion object {
        const val PAUSED_ALPHA = 0.7f
        const val PLAYING_ALPHA = 1.0f
    }

    abstract val binding: SongViewBinding

    protected lateinit var mSong: SongWrapper

    @CallSuper
    open fun setSong(song: SongWrapper, onSongClickCallback: () -> Unit) {
        mSong = song
        with(binding) {
            title.text = song.title
            artist.text = song.artist
            duration.text = TimeConverter().durationToString(song.duration)
            songRoot.setOnClickListener { onSongClickCallback() }
            songIcon.setImageBitmap(song.art)
        }
    }

    fun bindAction(onActionClickedCallback: (Song) -> Unit) {
        binding.action.setOnClickListener {
            onActionClickedCallback(mSong)
        }
    }

    fun updateCurrentSong(song: SongWrapper): Boolean {
        return when (mSong) {
            song -> {
                displayAsSelected()
                true
            }
            else -> {
                displayAsNotSelected()
                false
            }
        }
    }

    fun updateIsPlaying(isPlaying: Boolean) {
        when (isPlaying) {
            true -> displayAsPlaying()
            false -> displayAsPaused()
        }
    }

    private fun displayAsSelected() {
        binding.playingIndicator.isVisible = true
        binding.artist.ellipsize = TextUtils.TruncateAt.MARQUEE
        binding.artist.isSelected = true
    }

    private fun displayAsNotSelected() {
        binding.playingIndicator.isVisible = false
        binding.artist.ellipsize = TextUtils.TruncateAt.END
        binding.artist.isSelected = false
    }

    private fun displayAsPaused() {
        binding.playingIndicator.alpha = PAUSED_ALPHA
    }

    private fun displayAsPlaying() {
        binding.playingIndicator.alpha = PLAYING_ALPHA
    }
}

abstract class SongViewHolder(
    private val songView: SongView
) : SongListItemViewHolder(songView) {

    @CallSuper
    fun bind(
        song: SongWrapper,
        onClickCallBack: () -> Unit,
        onActionClickedCallback: ((Song) -> Unit)? = null
    ) {
        songView.setSong(song) { onClickCallBack() }
        if (onActionClickedCallback != null) {
            songView.bindAction(onActionClickedCallback)
        }
    }
}
