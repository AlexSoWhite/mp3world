package com.nafanya.mp3world.features.playerView.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
import com.nafanya.mp3world.features.downloading.DownloadViewModel
import com.nafanya.mp3world.features.downloading.ResultType
import com.nafanya.mp3world.features.favorites.viewModel.FavouriteListViewModel
import com.nafanya.mp3world.features.playerView.view.currentPlaylist.CurrentPlaylistDialogFragment
import javax.inject.Inject

class FullscreenControlsFragment : BaseFragment<PlayerControlViewFullscreenFragmentBinding>() {

    private var previousColor: Int = -1
    private var isColorInitialized = false

    @Inject
    lateinit var favoriteViewModel: FavouriteListViewModel
    @Inject
    lateinit var downloadViewModel: DownloadViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel: PlayerViewModel by viewModels { factory }

    private lateinit var application: Application
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
        application = requireActivity().application
        (application as PlayerApplication)
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
            findViewById<ConstraintLayout>(R.id.control_track_info_wrapper)
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
            val durationValue = song.duration
            val durationView = findViewById<TextView>(R.id.duration)
            val timeView = findViewById<TextView>(R.id.time)
            binding.controlsFullscreen.apply {
                durationView.text = timeConverter.durationToString(durationValue)
                setProgressUpdateListener { position, _ ->
                    timeView.text = timeConverter.durationToString(position)
                }
                val songIcon = findViewById<ShapeableImageView>(R.id.control_song_icon)
                songIcon?.setImageBitmap(song.art)
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
                    actionButton.setImageResource(R.drawable.download_icon)
                    actionButton.setOnClickListener {
                        Toast.makeText(
                            requireActivity(),
                            "загрузка начата",
                            Toast.LENGTH_SHORT
                        ).show()
                        downloadViewModel.download(song as RemoteSong) {
                            if (it.type == ResultType.SUCCESS) {
                                Toast.makeText(
                                    application,
                                    "${song.artist} - ${song.title} загружено",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    application,
                                    "ошибка",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            downloadViewModel.updateSongList(it)
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Suppress("NestedBlockDepth")
    private fun animateChanges(art: Bitmap? = null) = with(view) {
        this?.let {
            val averageColor = if (art != null) {
                ColorExtractor.getAverageColorWithNoWhiteComponent(art)
            } else {
                Color.parseColor(defaultBackgroundColor)
            }
            val colorFrom = if (isColorInitialized) {
                previousColor
            } else {
                context.getColor(android.R.color.transparent)
            }
            previousColor = averageColor
            isColorInitialized = true
            val root = findViewById<ConstraintLayout>(R.id.root)
            val colorAnimation =
                ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, averageColor)
            colorAnimation.duration = backgroundDuration
            colorAnimation.addUpdateListener {
                val color = (it.animatedValue as Int).toColor()
                var controlsBlue = color.blue()
                var controlsRed = color.red()
                var controlsGreen = color.green()
                controlsRed = (controlsRed + 2.0f) / componentsAmount
                controlsGreen = (controlsGreen + 2.0f) / componentsAmount
                controlsBlue = (controlsBlue + 2.0f) / componentsAmount
                val controlsColor =
                    Color.valueOf(controlsRed, controlsGreen, controlsBlue).toArgb()
                val backgroundColor = it.animatedValue as Int
                root.setBackgroundColor(backgroundColor)
                updateBarsColor(backgroundColor)
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

    private fun updateBarsColor(color: Int) {
        requireActivity().window.statusBarColor = color
        requireActivity().window.navigationBarColor = color
    }

    override fun onDestroyView() {
        binding.controlsFullscreen.player = null
        super.onDestroyView()
    }
}
