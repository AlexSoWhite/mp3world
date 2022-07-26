package com.nafanya.mp3world.features.playerView.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.material.imageview.ShapeableImageView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.player.Song
import com.nafanya.mp3world.core.utils.ColorExtractor
import com.nafanya.mp3world.core.utils.timeConverters.TimeConverter
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.databinding.PlayerViewFullscreenBinding
import com.nafanya.mp3world.features.downloading.DownloadViewModel
import com.nafanya.mp3world.features.downloading.ResultType
import com.nafanya.mp3world.features.favorites.FavouriteListManager
import com.nafanya.mp3world.features.favorites.viewModel.FavouriteListViewModel
import com.nafanya.mp3world.features.playlists.playlist.view.CurrentPlaylistDialogActivity
import javax.inject.Inject

class FullScreenPlayerActivity : AppCompatActivity() {

    private lateinit var binding: PlayerViewFullscreenBinding
    private var previousColor: Int = -1
    private var isColorInitialized = false

    @Inject
    lateinit var favoriteViewModel: FavouriteListViewModel
    @Inject
    lateinit var downloadViewModel: DownloadViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PlayerApplication).applicationComponent
            .playerViewComponent()
            .inject(this)
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.player_view_fullscreen)
        viewModel = factory.create(PlayerViewModel::class.java)
        viewModel.isPlayerInitialised.observe(this) {
            if (it) {
                viewModel.currentSong.observe(this) { song ->
                    renderSong(song)
                }
                binding.controlsFullscreen.currentPlaylist.setOnClickListener {
                    val intent = Intent(this, CurrentPlaylistDialogActivity::class.java)
                    startActivity(intent)
                }
                binding.controlsFullscreen.controlsFullscreen.showShuffleButton = true
            }
        }
        // animate elements arrive in list
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = alphaDuration
        alphaAnimation.startOffset = startOffset
        binding.controlsFullscreen.root.startAnimation(alphaAnimation)
    }

    private fun renderSong(song: Song) {
        binding.controlsFullscreen.song = song
        val timeConverter = TimeConverter()
        val durationValue = song.duration!!
        binding.controlsFullscreen.apply {
            duration.text =
                timeConverter.durationToString(durationValue)
            binding.controlsFullscreen.controlsFullscreen.setProgressUpdateListener { position, _ ->
                binding.controlsFullscreen.time.text =
                    timeConverter.durationToString(position)
            }
            val songIcon = binding.controlSongIcon
            when {
                song.art != null -> {
                    songIcon.setImageBitmap(song.art)
                    animateChanges(song.art)
                }
                song.artUrl != null -> {
                    Glide.with(songIcon).load(song.artUrl).into(songIcon)
                    binding.fullScreenPlayerView.setBackgroundColor(
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
                    if (list.contains(viewModel.currentSong.value)) {
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
                            viewModel.currentSong.value!!
                        )
                        isFavourite = true
                        favouriteButton.setImageResource(R.drawable.favorite)
                    } else {
                        favoriteViewModel.deleteFavourite(
                            viewModel.currentSong.value!!
                        )
                        isFavourite = false
                        favouriteButton.setImageResource(R.drawable.favorite_border)
                    }
                }
            } else {
                favouriteButton.setImageResource(R.drawable.download_icon)
                favouriteButton.setOnClickListener {
                    Toast.makeText(
                        this@FullScreenPlayerActivity,
                        "загрузка начата",
                        Toast.LENGTH_SHORT
                    ).show()
                    downloadViewModel.download(song) {
                        if (it.type == ResultType.SUCCESS) {
                            Toast.makeText(
                                this@FullScreenPlayerActivity,
                                "${song.artist} - ${song.title} загружено",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@FullScreenPlayerActivity,
                                "ошибка",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.controlsFullscreen.root.visibility = View.INVISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            this.finish()
            return true
        }
        return false
    }

    private fun animateChanges(art: Bitmap? = null) {
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
            val playPauseButton = findViewById<ShapeableImageView>(R.id.exo_play_pause)
            val prevButton = findViewById<ShapeableImageView>(R.id.exo_prev)
            val nextButton = findViewById<ShapeableImageView>(R.id.exo_next)
            val repeatButton = findViewById<ShapeableImageView>(R.id.exo_repeat_toggle)
            val playlistButton = findViewById<ShapeableImageView>(R.id.current_playlist)
            val favouriteButton = findViewById<ShapeableImageView>(R.id.favourite_button)
            val shuffleButton = findViewById<ShapeableImageView>(R.id.exo_shuffle)
            val progressBar = findViewById<DefaultTimeBar>(R.id.exo_progress)
            val title = findViewById<TextView>(R.id.track_title)
            val artist = findViewById<TextView>(R.id.track_artist)
            val duration = findViewById<TextView>(R.id.duration)
            val time = findViewById<TextView>(R.id.time)
            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, averageColor)
            colorAnimation.duration = backgroundDuration
            colorAnimation.addUpdateListener {
                val color = (it.animatedValue as Int).toColor()
                var controlsBlue = color.blue()
                var controlsRed = color.red()
                var controlsGreen = color.green()
                controlsRed = (controlsRed + 2.0f) / componentsAmount
                controlsGreen = (controlsGreen + 2.0f) / componentsAmount
                controlsBlue = (controlsBlue + 2.0f) / componentsAmount
                val controlsColor = Color.valueOf(controlsRed, controlsGreen, controlsBlue).toArgb()
                val backgroundColor = it.animatedValue as Int
                binding.fullScreenPlayerView.setBackgroundColor(backgroundColor)
                playPauseButton.setColorFilter(controlsColor)
                prevButton.setColorFilter(controlsColor)
                nextButton.setColorFilter(controlsColor)
                repeatButton.setColorFilter(controlsColor)
                playlistButton.setColorFilter(controlsColor)
                favouriteButton.setColorFilter(controlsColor)
                shuffleButton.setColorFilter(controlsColor)
                progressBar.setScrubberColor(controlsColor)
                title.setTextColor(controlsColor)
                artist.setTextColor(controlsColor)
                duration.setTextColor(controlsColor)
                time.setTextColor(controlsColor)
            }
            colorAnimation.start()
        } else {
            setTheme(R.style.exoPlayer)
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
