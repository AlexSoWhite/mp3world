package com.nafanya.mp3world.features.playerView.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColor
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.material.imageview.ShapeableImageView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.utils.ColorExtractor
import com.nafanya.mp3world.core.utils.timeConverters.TimeConverter
import com.nafanya.mp3world.features.downloading.DownloadViewModel
import com.nafanya.mp3world.features.downloading.ResultType
import com.nafanya.mp3world.features.favorites.FavouriteListManager
import com.nafanya.mp3world.features.favorites.viewModel.FavouriteListViewModel
import com.nafanya.mp3world.features.playlists.playlist.view.CurrentPlaylistDialogActivity
import com.nafanya.player.PlayerInteractor
import com.nafanya.player.Song
import javax.inject.Inject

class FullscreenControlsFragment : Fragment() {

    private var previousColor: Int = -1
    private var isColorInitialized = false

    @Inject
    lateinit var favoriteViewModel: FavouriteListViewModel
    @Inject
    lateinit var downloadViewModel: DownloadViewModel
    @Inject
    lateinit var playerInteractor: PlayerInteractor

    private lateinit var controlsFullScreen: StyledPlayerControlView
    private val controls = mutableListOf<View>()

    override fun onAttach(context: Context) {
        (requireActivity().application as PlayerApplication)
            .applicationComponent
            .playerViewComponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.full_screen_player_controls_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controlsFullScreen = requireActivity().findViewById(R.id.controls_fullscreen)
        controlsFullScreen.showTimeoutMs = 0
        controlsFullScreen.player = playerInteractor.player
        playerInteractor.isPlayerInitialised.observe(viewLifecycleOwner) {
            if (it) {
                playerInteractor.currentSong.observe(viewLifecycleOwner) { song ->
                    renderSong(song)
                }
                requireActivity().findViewById<ShapeableImageView>(R.id.current_playlist).setOnClickListener {
                    val intent = Intent(requireActivity(), CurrentPlaylistDialogActivity::class.java)
                    startActivity(intent)
                }
                controlsFullScreen.showShuffleButton = true
            }
        }
        // animate elements arrive in list
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = alphaDuration
        alphaAnimation.startOffset = startOffset
        with(requireActivity()) {
            controls.addAll(
                listOf(
                    findViewById<ShapeableImageView>(R.id.exo_play_pause),
                    findViewById<ShapeableImageView>(R.id.exo_prev),
                    findViewById<ShapeableImageView>(R.id.exo_next),
                    findViewById<ShapeableImageView>(R.id.exo_repeat_toggle),
                    findViewById<ShapeableImageView>(R.id.current_playlist),
                    findViewById<ShapeableImageView>(R.id.favourite_button),
                    findViewById<ShapeableImageView>(R.id.exo_shuffle),
                    findViewById<DefaultTimeBar>(R.id.exo_progress),
                    findViewById<TextView>(R.id.control_fullscreen_track_title),
                    findViewById<TextView>(R.id.control_fullscreen_track_artist),
                    findViewById<TextView>(R.id.duration),
                    findViewById<TextView>(R.id.time)
                )
            )
            findViewById<ConstraintLayout>(R.id.controls_wrapper).startAnimation(alphaAnimation)
        }
    }

    private fun renderSong(song: Song) {
        requireActivity().findViewById<TextView>(
            R.id.control_fullscreen_track_title
        ).text = song.title
        requireActivity().findViewById<TextView>(
            R.id.control_fullscreen_track_artist
        ).text = song.artist
        val timeConverter = TimeConverter()
        val durationValue = song.duration!!
        val durationView = requireActivity().findViewById<TextView>(R.id.duration)
        val timeView = requireActivity().findViewById<TextView>(R.id.time)
        controlsFullScreen.apply {
            durationView.text = timeConverter.durationToString(durationValue)
            controlsFullScreen.setProgressUpdateListener { position, _ ->
                timeView.text = timeConverter.durationToString(position)
            }
            val songIcon = requireActivity().findViewById<ShapeableImageView>(R.id.control_song_icon)
            when {
                song.art != null -> {
                    songIcon.setImageBitmap(song.art)
                    animateChanges(song.art)
                }
                song.artUrl != null -> {
                    Glide.with(songIcon).load(song.artUrl).into(songIcon)
                    requireActivity().findViewById<ConstraintLayout>(R.id.root).setBackgroundColor(
                        Color.parseColor(defaultBackgroundColor)
                    )
                }
                else -> {
                    songIcon.setImageResource(R.drawable.default_placeholder)
                    animateChanges()
                }
            }
            // favourite
            val favouriteButton = findViewById<ShapeableImageView>(R.id.favourite_button)
            if (song.url == null) {
                var isFavourite = false
                FavouriteListManager.favorites.value?.songList?.let { list ->
                    if (list.contains(playerInteractor.currentSong.value)) {
                        isFavourite = true
                    }
                }
                if (!isFavourite) {
                    favouriteButton.setImageResource(R.drawable.favorite_border)
                } else {
                    favouriteButton.setImageResource(R.drawable.favorite)
                }
                favouriteButton.setOnClickListener {
                    if (!isFavourite) {
                        favoriteViewModel.addFavourite(
                            playerInteractor.currentSong.value!!
                        )
                        isFavourite = true
                        favouriteButton.setImageResource(R.drawable.favorite)
                    } else {
                        favoriteViewModel.deleteFavourite(
                            playerInteractor.currentSong.value!!
                        )
                        isFavourite = false
                        favouriteButton.setImageResource(R.drawable.favorite_border)
                    }
                }
            } else {
                favouriteButton.setImageResource(R.drawable.download_icon)
                favouriteButton.setOnClickListener {
                    Toast.makeText(
                        requireActivity(),
                        "загрузка начата",
                        Toast.LENGTH_SHORT
                    ).show()
                    downloadViewModel.download(song) {
                        if (it.type == ResultType.SUCCESS) {
                            Toast.makeText(
                                requireActivity(),
                                "${song.artist} - ${song.title} загружено",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "ошибка",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun animateChanges(art: Bitmap? = null) {
        with(requireActivity()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val averageColor = if (art != null) {
                    ColorExtractor.getAverageColorWithNoWhiteComponent(art)
                } else {
                    Color.parseColor(defaultBackgroundColor)
                }
                val colorFrom = if (isColorInitialized) {
                    previousColor
                } else {
                    getColor(android.R.color.transparent)
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
                    controls.forEach { view ->
                        when (view) {
                            is ShapeableImageView -> view.setColorFilter(controlsColor)
                            is DefaultTimeBar -> view.setScrubberColor(controlsColor)
                            is TextView -> view.setTextColor(controlsColor)
                        }
                    }
                }
                colorAnimation.start()
            } else {
                setTheme(R.style.exoPlayer)
            }
        }
    }

    companion object {
        private const val alphaDuration = 500L
        private const val backgroundDuration = 600L
        private const val startOffset = 350L
        private const val defaultBackgroundColor = "#373232"
        private const val componentsAmount = 3
    }
}
