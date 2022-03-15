package com.nafanya.mp3world.view.playerViews

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.imageview.ShapeableImageView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.PlayerViewFullscreenBinding
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.listManagers.FavouriteListManager
import com.nafanya.mp3world.model.localStorage.LocalStorageProvider
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.view.OnSwipeListener
import com.nafanya.mp3world.view.listActivities.playlists.CurrentPlaylistDialogActivity
import kotlin.concurrent.thread

class FullScreenPlayerActivity : AppCompatActivity() {

    private lateinit var binding: PlayerViewFullscreenBinding
    private var playerView: FullScreenPlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.player_view_fullscreen)
        OnSwipeListener(binding.root) {
            finish()
        }
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
        ForegroundServiceLiveDataProvider.isPlayerInitialized.observe(
            this,
            observerPlayer
        )
    }

    inner class FullScreenPlayerView(
        private val activity: AppCompatActivity,
        layoutResId: Int
    ) : GenericPlayerControlView(activity, layoutResId) {

        override fun setSongObserver() {
            // observe current song state
            val songObserver = Observer<Song> { song ->
                activity.findViewById<TextView>(R.id.track_title).text = song.title
                activity.findViewById<TextView>(R.id.track_artist).text = song.artist
                thread {
                    if (song.art != null) {
                        activity.findViewById<ImageView>(
                            R.id.control_song_icon
                        ).setImageBitmap(song.art)
                    } else {
                        activity.findViewById<ImageView>(R.id.control_song_icon)
                            .setImageResource(R.drawable.default_placeholder)
                    }
                }
                // favourite
                var isFavourite = false
                FavouriteListManager.songList.value?.let { list ->
                    if (list.contains(ForegroundServiceLiveDataProvider.currentSong.value)) {
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
                            ForegroundServiceLiveDataProvider.currentSong.value!!
                        )
                        LocalStorageProvider.addFavourite(
                            activity,
                            ForegroundServiceLiveDataProvider.currentSong.value!!
                        )
                        isFavourite = true
                        binding.favouriteButton.setImageResource(R.drawable.favorite)
                    } else {
                        FavouriteListManager.delete(
                            ForegroundServiceLiveDataProvider.currentSong.value!!
                        )
                        LocalStorageProvider.deleteFavourite(
                            activity,
                            ForegroundServiceLiveDataProvider.currentSong.value!!
                        )
                        isFavourite = false
                        binding.favouriteButton.setImageResource(R.drawable.favorite_border)
                    }
                }
            }
            ForegroundServiceLiveDataProvider.currentSong.observe(activity, songObserver)
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
}
