package com.nafanya.mp3world.presentation.player_view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.util.RepeatModeUtil
import androidx.media3.ui.DefaultTimeBar
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.presentation.core.common_ui.BaseFragment
import com.nafanya.mp3world.core.coroutines.collectInScope
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.utils.animators.AoedeAlphaAnimation
import com.nafanya.mp3world.core.utils.time_converters.TimeConverter
import com.nafanya.mp3world.presentation.core.images.glide.CustomBitmapTarget
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.databinding.PlayerControlViewFullscreenFragmentBinding
import com.nafanya.mp3world.data.downloading.api.download
import com.nafanya.mp3world.presentation.player_view.current_playlist.CurrentPlaylistDialogFragment
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import androidx.core.graphics.toColorInt

@Suppress("TooManyFunctions")
class FullscreenControlsFragment : BaseFragment<PlayerControlViewFullscreenFragmentBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: PlayerViewModel by viewModels { factory }

    private val controls = mutableListOf<View>()

    companion object {
        private const val BACKGROUND_DURATION = 600L
        private const val DEFAULT_BACKGROUND_COLOR = "#767685"
        private const val COMPONENTS_AMOUNT = 3

        private const val TAG = "_FullScreenControls"
    }

    private var previousColor: Int = DEFAULT_BACKGROUND_COLOR.toColorInt()

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
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.isPlayerReady.collectLatest {
                        Log.d(TAG, "isPlayerReady: $it, player: ${viewModel.player}")
                        binding.controlsFullscreen.player = if (it) {
                            viewModel.player
                        } else {
                            null
                        }
                    }
                }
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.currentSong.filterNotNull().collectLatest {
                        Log.d(TAG, "song received: $it")
                        renderSong(it as SongWrapper)
                    }
                }
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
                    findViewById<ShapeableImageView>(R.id.exo_play),
                    findViewById<ShapeableImageView>(R.id.exo_pause),
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
            .placeholder(R.drawable.song_icon_preview)
            .into(
                CustomBitmapTarget(
                    {
                        imageView.setImageBitmap(it)
                        processColor(it)
                    }
                )
            )
    }

    private fun processColor(resource: Bitmap) {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            animateChanges(resource)
        }
    }

    private suspend fun animateChanges(art: Bitmap) = with(requireView()) {
        val averageColor = viewModel.getAverageColorWithNoWhiteComponent(art)
        val colorFrom = previousColor
        previousColor = averageColor
        val root = findViewById<ConstraintLayout>(R.id.root)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, averageColor)
        colorAnimation.duration = BACKGROUND_DURATION
        colorAnimation.addUpdateListener {
            val controlsColor = calculateControlsColor(
                (it.animatedValue as Int)
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

    private fun calculateControlsColor(color: Int): Int {
        val controlsRed = (Color.red(color) + 2 * 255)  / (COMPONENTS_AMOUNT * 255.toFloat())
        val controlsGreen = (Color.green(color) + 2 * 255) / (COMPONENTS_AMOUNT * 255.toFloat())
        val controlsBlue = (Color.blue(color) + 2 * 255) / (COMPONENTS_AMOUNT * 255.toFloat())
        return Color.valueOf(controlsRed, controlsGreen, controlsBlue).toArgb()
    }

    private fun updateBarsColor(color: Int) {
        activity?.window?.statusBarColor = color
        activity?.window?.navigationBarColor = color
    }

    override fun onDestroyView() {
        binding.controlsFullscreen.player = null
        controls.clear()
        super.onDestroyView()
    }
}
