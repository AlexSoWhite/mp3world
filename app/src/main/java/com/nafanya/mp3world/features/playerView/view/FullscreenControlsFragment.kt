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
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.util.RepeatModeUtil
import com.google.android.material.imageview.ShapeableImageView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.utils.ColorExtractor
import com.nafanya.mp3world.core.utils.animators.AoedeAlphaAnimation
import com.nafanya.mp3world.core.utils.timeConverters.TimeConverter
import com.nafanya.mp3world.core.view.BaseFragment
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong
import com.nafanya.mp3world.databinding.PlayerControlViewFullscreenFragmentBinding
import com.nafanya.mp3world.features.downloading.DownloadingView
import com.nafanya.mp3world.features.downloading.DownloadingViewModel
import com.nafanya.mp3world.features.favorites.viewModel.FavouriteListViewModel
import com.nafanya.mp3world.features.playerView.view.currentPlaylist.CurrentPlaylistDialogFragment
import javax.inject.Inject

class FullscreenControlsFragment :
    BaseFragment<PlayerControlViewFullscreenFragmentBinding>(),
    DownloadingView {

    private var previousColor: Int = -1
    private var isColorInitialized = false

    @Inject
    lateinit var favoriteViewModel: FavouriteListViewModel

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: PlayerViewModel by viewModels { factory }
    override val downloadingViewModel: DownloadingViewModel
        get() = viewModel

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
        viewModel.currentSong.observe(viewLifecycleOwner) {
            renderSong(it as SongWrapper)
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

    @Suppress("LongMethod", "NestedBlockDepth", "ComplexMethod")
    private fun renderSong(song: SongWrapper) = with(view) {
        this?.let {
            val titleView = findViewById<TextView>(R.id.control_fullscreen_track_title)
            titleView?.text = song.title
            titleView?.isSelected = true
            val artistView = findViewById<TextView>(R.id.control_fullscreen_track_artist)
            artistView?.text = song.artist
            artistView?.isSelected = true
            val timeConverter = TimeConverter()
            val durationView = findViewById<TextView>(R.id.duration)
            val timeView = findViewById<TextView>(R.id.time)
            binding.controlsFullscreen.apply {
                durationView.text = timeConverter.durationToString(song.duration)
                setProgressUpdateListener { position, _ ->
                    timeView.text = timeConverter.durationToString(position)
                }
                adjustImage(song.art)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    animateChanges(song.art)
                } else {
                    binding.root.setBackgroundColor(
                        Color.parseColor(defaultBackgroundColor)
                    )
                    updateBarsColor(Color.parseColor(defaultBackgroundColor))
                }
                // favourite
                val actionButton = findViewById<ShapeableImageView>(R.id.action_button)
                if (song is LocalSong) {
                    favoriteViewModel.isSongInFavourite(song).observe(viewLifecycleOwner) {
                        if (it) {
                            actionButton.setImageResource(R.drawable.favorite)
                            actionButton.setOnClickListener {
                                favoriteViewModel.deleteFavourite(song)
                            }
                        } else {
                            actionButton.setImageResource(R.drawable.favorite_border)
                            actionButton.setOnClickListener {
                                favoriteViewModel.addFavourite(song)
                            }
                        }
                    }
                } else {
                    actionButton.setImageResource(R.drawable.ic_download)
                    actionButton.setOnClickListener {
                        download(requireActivity(), song as RemoteSong)
                    }
                }
            }
        }
    }

    private fun adjustImage(bitmap: Bitmap) {
        view?.findViewById<ShapeableImageView>(R.id.control_song_icon)?.setImageBitmap(bitmap)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Suppress("NestedBlockDepth")
    private fun animateChanges(art: Bitmap) = with(view) {
        this?.let {
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
