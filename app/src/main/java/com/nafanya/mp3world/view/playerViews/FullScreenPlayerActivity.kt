package com.nafanya.mp3world.view.playerViews

import android.content.Intent
import android.os.Bundle
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
import com.google.android.material.imageview.ShapeableImageView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.PlayerViewFullscreenBinding
import com.nafanya.mp3world.model.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.model.listManagers.FavouriteListManager
import com.nafanya.mp3world.model.localStorage.LocalStorageProvider
import com.nafanya.mp3world.model.network.Downloader
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.view.listActivities.playlists.CurrentPlaylistDialogActivity
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class FullScreenPlayerActivity : AppCompatActivity() {

    private lateinit var binding: PlayerViewFullscreenBinding
    private var playerView: FullScreenPlayerView? = null

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
        alphaAnimation.duration = duration
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
                    }
                    song.artUrl != null -> {
                        Glide.with(songIcon)
                            .load(song.artUrl)
                            .into(songIcon)
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
        private const val duration = 500L
        private const val startOffset = 350L
    }
}
