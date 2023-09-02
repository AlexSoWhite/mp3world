package com.nafanya.mp3world.features.songListViews.baseViews

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.nafanya.mp3world.R
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

    protected var mSong: SongWrapper? = null

    private var mOnSongSelectedListener: ((SongView) -> Unit)? = null

    @CallSuper
    open fun setSong(song: SongWrapper, onSongClickCallback: () -> Unit) {
        mSong = song
        with(binding) {
            title.text = song.title
            artist.text = song.artist
            duration.text = TimeConverter.durationToString(song.duration)
            songRoot.setOnClickListener { onSongClickCallback() }
            Glide.with(context).load(song).placeholder(R.drawable.song_icon_preview).into(songIcon)
        }
    }

    fun bindAction(onActionClickedCallback: (Song) -> Unit) {
        binding.action.setOnClickListener {
            mSong?.let {
                onActionClickedCallback(it)
            }
        }
    }

    fun updateCurrentSong(song: SongWrapper): Boolean {
        return if (mSong == song) {
            mOnSongSelectedListener?.invoke(this)
            displayAsSelected()
            true
        } else {
            displayAsNotSelected()
            false
        }
    }

    fun updateIsPlaying(isPlaying: Boolean) {
        when (isPlaying) {
            true -> displayAsPlaying()
            false -> displayAsPaused()
        }
    }

    fun setOnSongSelectedListener(callback: (SongView) -> Unit) {
        mOnSongSelectedListener = callback
    }

    private fun displayAsSelected() {
        binding.playingIndicator.isVisible = true
        binding.selectedIndicator.isVisible = true
        binding.artist.ellipsize = TextUtils.TruncateAt.MARQUEE
        binding.artist.isSelected = true
    }

    private fun displayAsNotSelected() {
        binding.playingIndicator.isVisible = false
        binding.selectedIndicator.isVisible = false
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
        onClickCallback: (SongView) -> Unit,
        onActionClickedCallback: ((Song) -> Unit)? = null
    ) {
        songView.setSong(song) { onClickCallback(songView) }
        if (onActionClickedCallback != null) {
            songView.bindAction(onActionClickedCallback)
        }
    }

    fun updateIsPlaying(song: SongWrapper): Boolean {
        return songView.updateCurrentSong(song)
    }
}
