package com.nafanya.mp3world.view.playerViews

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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColor
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.material.imageview.ShapeableImageView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.PlayerViewFullscreenBinding
import com.nafanya.mp3world.model.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.model.listManagers.FavouriteListManager
import com.nafanya.mp3world.model.network.ResultType
import com.nafanya.mp3world.model.timeConverters.TimeConverter
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.view.ColorExtractor
import com.nafanya.mp3world.view.listActivities.playlists.CurrentPlaylistDialogActivity
import com.nafanya.mp3world.viewmodel.DownloadViewModel
import com.nafanya.mp3world.viewmodel.FavouriteListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FullScreenPlayerActivity : AppCompatActivity() {

    private lateinit var binding: PlayerViewFullscreenBinding
    private lateinit var favoriteViewModel: FavouriteListViewModel
    private lateinit var downloadViewModel: DownloadViewModel
    private var playerView: FullScreenPlayerView? = null
    private var previousColor: Int = -1
    private var isColorInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.player_view_fullscreen)
        favoriteViewModel = ViewModelProvider(this)[FavouriteListViewModel::class.java]
        downloadViewModel = ViewModelProvider(this)[DownloadViewModel::class.java]
        val observerPlayer = Observer<Boolean> {
            if (it) {
                playerView = FullScreenPlayerView(this, R.id.player_control_fullscreen_view)
                playerView!!.setSongObserver()
                findViewById<ShapeableImageView>(R.id.current_playlist).setOnClickListener {
                    val intent = Intent(this, CurrentPlaylistDialogActivity::class.java)
                    startActivity(intent)
                }
                playerView!!.playerControlView.showShuffleButton = true
            }
        }
        PlayerLiveDataProvider.isPlayerInitialized.observe(
            this,
            observerPlayer
        )
        // animate elements arrive in list
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = alphaDuration
        alphaAnimation.startOffset = startOffset
        findViewById<ConstraintLayout>(R.id.controls_fullscreen).startAnimation(alphaAnimation)
    }

    inner class FullScreenPlayerView(
        private val activity: AppCompatActivity,
        layoutResId: Int
    ) : GenericPlayerControlView(activity, layoutResId) {

        @Suppress("LongMethod")
        override fun setSongObserver() {
            // observe current song state
            val songObserver = Observer<Song> { song ->
                activity.findViewById<TextView>(R.id.track_title).text = song.title
                activity.findViewById<TextView>(R.id.track_artist).text = song.artist
                val timeConverter = TimeConverter()
                val duration = song.duration!!
                activity.findViewById<TextView>(R.id.duration).text =
                    timeConverter.durationToString(duration)
                playerControlView.setProgressUpdateListener { position, _ ->
                    activity.findViewById<TextView>(R.id.time).text =
                        timeConverter.durationToString(position)
                }
                activity.findViewById<TextView>(R.id.time).text
                val songIcon = activity.findViewById<ImageView>(R.id.control_song_icon)
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
                        if (list.contains(PlayerLiveDataProvider.currentSong.value)) {
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
                                PlayerLiveDataProvider.currentSong.value!!
                            )
                            isFavourite = true
                            favouriteButton.setImageResource(R.drawable.favorite)
                        } else {
                            favoriteViewModel.deleteFavourite(
                                PlayerLiveDataProvider.currentSong.value!!
                            )
                            isFavourite = false
                            favouriteButton.setImageResource(R.drawable.favorite_border)
                        }
                    }
                } else {
                    favouriteButton.setImageResource(R.drawable.download_icon)
                    favouriteButton.setOnClickListener {
                        Toast.makeText(
                            activity,
                            "загрузка начата",
                            Toast.LENGTH_SHORT
                        ).show()
                        downloadViewModel.download(song) {
                            if (it.type == ResultType.SUCCESS) {
                                Toast.makeText(
                                    activity,
                                    "${song.artist} - ${song.title} загружено",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    activity,
                                    "ошибка",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
            PlayerLiveDataProvider.currentSong.observe(activity, songObserver)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.playerControlFullscreenView.visibility = View.INVISIBLE
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
                Color.parseColor(defaultBackgroundColor)
            }
            previousColor = averageColor
            isColorInitialized = true
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
                findViewById<ConstraintLayout>(R.id.controls_fullscreen).children.forEach { view ->
                    when (view) {
                        is ShapeableImageView -> {
                            view.setColorFilter(controlsColor)
                        }
                        is DefaultTimeBar -> {
                            view.setScrubberColor(controlsColor)
                        }
                        is TextView -> {
                            view.setTextColor(controlsColor)
                        }
                    }
                }
            }
            colorAnimation.start()
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
