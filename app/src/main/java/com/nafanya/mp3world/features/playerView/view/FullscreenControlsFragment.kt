package com.nafanya.mp3world.features.playerView.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColor
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.util.RepeatModeUtil
import com.google.android.material.imageview.ShapeableImageView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.commonUi.BaseFragment
import com.nafanya.mp3world.core.coroutines.collectInScope
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.utils.ColorExtractor
import com.nafanya.mp3world.core.utils.animators.AoedeAlphaAnimation
import com.nafanya.mp3world.core.utils.timeConverters.TimeConverter
import com.nafanya.mp3world.core.wrappers.images.glide.CustomBitmapTarget
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.databinding.PlayerControlViewFullscreenFragmentBinding
import com.nafanya.mp3world.features.downloading.api.download
import com.nafanya.mp3world.features.playerView.view.currentPlaylist.CurrentPlaylistDialogFragment
import javax.inject.Inject

class FullscreenControlsFragment : BaseFragment<PlayerControlViewFullscreenFragmentBinding>() {

    private var previousColor: Int = -1
    private var isColorInitialized = false

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: PlayerViewModel by viewModels { factory }

    private val controls = mutableListOf<View>()

    companion object {
        private const val backgroundDuration = 600L
        private const val defaultBackgroundColor = "#373232"
        private const val componentsAmount = 3
    }

    override fun inflate(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): PlayerControlViewFullscreenFragmentBinding {
        return PlayerControlViewFullscreenFragmentBinding.inflate(inflater, parent, attachToParent)
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as PlayerApplication)
            .applicationComponent
            .playerViewComponent
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.controlsFullscreen) {
            showTimeoutMs = 0
            repeatToggleModes =
                RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL or
                RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE or
                RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE
            showShuffleButton = true
        }
        lifecycleScope.launchWhenCreated {
            viewModel.isPlayerInitialised.collect {
                binding.controlsFullscreen.player = viewModel.player
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.currentSong.collect {
                renderSong(it as SongWrapper)
            }
        }
        view.findViewById<ShapeableImageView>(R.id.current_playlist)
            .setOnClickListener {
                CurrentPlaylistDialogFragment().show(childFragmentManager, null)
            }
        // animate elements arrive in list
        with(view) {
            controls.addAll(
                listOf(
                    findViewById<ShapeableImageView>(R.id.exo_play_pause),
                    findViewById<ShapeableImageView>(R.id.exo_prev),
                    findViewById<ShapeableImageView>(R.id.exo_next),
                    findViewById<ShapeableImageView>(R.id.exo_repeat_toggle),
                    findViewById<ShapeableImageView>(R.id.current_playlist),
                    findViewById<ShapeableImageView>(R.id.action_button),
                    findViewById<ShapeableImageView>(R.id.exo_shuffle),
                    findViewById<DefaultTimeBar>(R.id.exo_progress),
                    findViewById<TextView>(R.id.control_fullscreen_track_title),
                    findViewById<TextView>(R.id.control_fullscreen_track_artist),
                    findViewById<TextView>(R.id.duration),
                    findViewById<TextView>(R.id.time)
                )
            )
            findViewById<ConstraintLayout>(R.id.controls_wrapper)
                .startAnimation(AoedeAlphaAnimation())
            findViewById<TextView>(R.id.control_fullscreen_track_title)
                .startAnimation(AoedeAlphaAnimation())
            findViewById<TextView>(R.id.control_fullscreen_track_artist)
                .startAnimation(AoedeAlphaAnimation())
        }
    }

    private fun renderSong(song: SongWrapper) = with(requireView()) {
        val titleView = findViewById<TextView>(R.id.control_fullscreen_track_title)
        titleView?.text = song.title
        titleView?.isSelected = true
        val artistView = findViewById<TextView>(R.id.control_fullscreen_track_artist)
        artistView?.text = song.artist
        artistView?.isSelected = true
        val durationView = findViewById<TextView>(R.id.duration)
        val timeView = findViewById<TextView>(R.id.time)
        binding.controlsFullscreen.apply {
            durationView.text = TimeConverter.durationToString(song.duration)
            setProgressUpdateListener { position, _ ->
                timeView.text = TimeConverter.durationToString(position)
            }
            adjustImage(song)
            // favourite
            val actionButton = findViewById<ShapeableImageView>(R.id.action_button)
            if (song is LocalSong) {
                setupFavourite(song, actionButton)
            } else {
                actionButton.setImageResource(R.drawable.icv_download)
                actionButton.setOnClickListener {
                    download(viewModel, song as RemoteSong)
                }
            }
        }
    }

    private fun setupFavourite(song: LocalSong, actionButton: ShapeableImageView) {
        viewModel.isSongInFavourites(song).collectInScope(lifecycleScope) {
            if (it) {
                actionButton.setImageResource(R.drawable.icv_favorite_filled)
                actionButton.setOnClickListener {
                    viewModel.deleteFavourite(song)
                }
            } else {
                actionButton.setImageResource(R.drawable.icv_favorite_border)
                actionButton.setOnClickListener {
                    viewModel.addFavourite(song)
                }
            }
        }
    }

    private fun adjustImage(song: SongWrapper) {
        val imageView = requireView().findViewById<ShapeableImageView>(R.id.control_song_icon)
        Glide.with(this)
            .asBitmap()
            .load(song)
            .into(
                CustomBitmapTarget(
                    {
                        imageView.setImageBitmap(it)
                        proceedColor(it)
                    }
                )
            )
    }

    private fun proceedColor(resource: Bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            animateChanges(resource)
        } else {
            binding.root.setBackgroundColor(
                Color.parseColor(defaultBackgroundColor)
            )
            updateBarsColor(Color.parseColor(defaultBackgroundColor))
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Suppress("NestedBlockDepth")
    private fun animateChanges(art: Bitmap) = with(requireView()) {
        val averageColor = ColorExtractor.getAverageColorWithNoWhiteComponent(art)
        val colorFrom = if (isColorInitialized) {
            previousColor
        } else {
            context.getColor(android.R.color.transparent)
        }
        previousColor = averageColor
        isColorInitialized = true
        val root = findViewById<ConstraintLayout>(R.id.root)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, averageColor)
        colorAnimation.duration = backgroundDuration
        colorAnimation.addUpdateListener {
            val controlsColor = calculateControlsColor(
                (it.animatedValue as Int).toColor()
            )
            root.setBackgroundColor(it.animatedValue as Int)
            updateBarsColor(it.animatedValue as Int)
            controls.forEach { v ->
                when (v) {
                    is ShapeableImageView -> v.setColorFilter(controlsColor)
                    is DefaultTimeBar -> v.setScrubberColor(controlsColor)
                    is TextView -> v.setTextColor(controlsColor)
                }
            }
        }
        colorAnimation.start()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun calculateControlsColor(color: Color): Int {
        val controlsRed = (color.red() + 2.0f) / componentsAmount
        val controlsGreen = (color.green() + 2.0f) / componentsAmount
        val controlsBlue = (color.blue() + 2.0f) / componentsAmount
        return Color.valueOf(controlsRed, controlsGreen, controlsBlue).toArgb()
    }

    private fun updateBarsColor(color: Int) {
        requireActivity().window.statusBarColor = color
        requireActivity().window.navigationBarColor = color
    }

    override fun onDestroyView() {
        binding.controlsFullscreen.player = null
        super.onDestroyView()
    }
}
