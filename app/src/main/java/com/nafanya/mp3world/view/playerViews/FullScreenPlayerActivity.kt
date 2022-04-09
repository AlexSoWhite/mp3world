package com.nafanya.mp3world.view.playerViews

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.imageview.ShapeableImageView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.PlayerViewFullscreenBinding
import com.nafanya.mp3world.model.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.model.listManagers.FavouriteListManager
import com.nafanya.mp3world.model.localStorage.LocalStorageProvider
import com.nafanya.mp3world.model.network.Downloader
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.view.ColorExtractor
import com.nafanya.mp3world.view.listActivities.playlists.CurrentPlaylistDialogActivity
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class FullScreenPlayerActivity : AppCompatActivity() {

    private lateinit var binding: PlayerViewFullscreenBinding
    private var playerView: FullScreenPlayerView? = null
    private var previousColor: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.player_view_fullscreen)
        val observerPlayer = Observer<Boolean> {
            if (it) {
                playerView = FullScreenPlayerView(this, R.id.player_control_fullscreen_view)
                playerView!!.setSongObserver()
                findViewById<ShapeableImageView>(R.id.current_playlist).setOnClickListener {
                    val intent = Intent(this, CurrentPlaylistDialogActivity::class.java)
                    startActivity(intent)
                }
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
        binding.favouriteButton.startAnimation(alphaAnimation)
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
                val songIcon = activity.findViewById<ImageView>(R.id.control_song_icon)
                when {
                    song.art != null -> {
                        songIcon.setImageBitmap(song.art)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val averageColor = ColorExtractor.getAverageColor(song.art).toArgb()
                            val colorFrom =
                                if (previousColor != -1) {
                                    previousColor
                                } else {
                                    previousColor = averageColor
                                    getColor(android.R.color.transparent)
                                }
                            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, averageColor)
                            colorAnimation.duration = backgroundDuration
                            colorAnimation.addUpdateListener {
                                binding.fullScreenPlayerView.setBackgroundColor(it.animatedValue as Int)
                            }
                            colorAnimation.start()
                        }
                    }
                    song.artUrl != null -> {
                        val bitmap = Glide.with(songIcon).asBitmap()
                        bitmap.addListener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Bitmap>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.d("Image", e.toString())
                                return false
                            }

                            override fun onResourceReady(
                                resource: Bitmap?,
                                model: Any?,
                                target: Target<Bitmap>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    songIcon.setImageBitmap(resource)
                                    val averageColor = ColorExtractor.getAverageColor(resource!!).toArgb()
                                    val colorFrom =
                                    if (previousColor != -1) {
                                        previousColor
                                    } else {
                                        previousColor = averageColor
                                        getColor(android.R.color.transparent)
                                    }
                                    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, averageColor)
                                    colorAnimation.duration = backgroundDuration
                                    colorAnimation.addUpdateListener {
                                        binding.fullScreenPlayerView.setBackgroundColor(it.animatedValue as Int)
                                    }
                                    colorAnimation.start()
                                    return true
                                }
                                return false
                            }
                        })
                        bitmap.load(song.artUrl).into(songIcon)
//                        val bitmap = Glide.with(songIcon).asBitmap()
//                        bitmap.load(song.artUrl).into(songIcon)
//                        Glide.with(songIcon).load(song.artUrl).into(songIcon)
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                            val averageColor = ColorExtractor.getAverageColor(bitmap)
//                            binding.fullScreenPlayerView.setBackgroundColor(averageColor.toArgb())
//                            songIcon.setImageBitmap(bitmap)
//                        }
                    }
                    else -> {
                        songIcon.setImageResource(R.drawable.default_placeholder)
                    }
                }
                // favourite
                if (song.url == null) {
                    var isFavourite = false
                    FavouriteListManager.favorites.value?.let { list ->
                        if (list.contains(PlayerLiveDataProvider.currentSong.value)) {
                            isFavourite = true
                        }
                    }
                    if (!isFavourite) {
                        binding.favouriteButton.setImageResource(R.drawable.favorite_border)
                    } else {
                        binding.favouriteButton.setImageResource(R.drawable.favorite)
                    }
                    binding.favouriteButton.setOnClickListener {
                        if (!isFavourite) {
                            FavouriteListManager.add(
                                PlayerLiveDataProvider.currentSong.value!!
                            )
                            LocalStorageProvider().addFavourite(
                                PlayerLiveDataProvider.currentSong.value!!
                            )
                            isFavourite = true
                            binding.favouriteButton.setImageResource(R.drawable.favorite)
                        } else {
                            FavouriteListManager.delete(
                                PlayerLiveDataProvider.currentSong.value!!
                            )
                            LocalStorageProvider().deleteFavourite(
                                PlayerLiveDataProvider.currentSong.value!!
                            )
                            isFavourite = false
                            binding.favouriteButton.setImageResource(R.drawable.favorite_border)
                        }
                    }
                } else {
                    binding.favouriteButton.setImageResource(R.drawable.download_icon)
                    binding.favouriteButton.setOnClickListener {
                        Toast.makeText(
                            activity,
                            "загрузка начата",
                            Toast.LENGTH_SHORT
                        ).show()
                        Downloader().downLoad(song) {
                            Toast.makeText(
                                activity,
                                "${song.artist} - ${song.title} загружено",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            PlayerLiveDataProvider.currentSong.observe(activity, songObserver)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            this.finish()
            return true
        }
        return false
    }

    companion object {
        private const val alphaDuration = 500L
        private const val backgroundDuration = 500L
        private const val startOffset = 350L
    }
}
