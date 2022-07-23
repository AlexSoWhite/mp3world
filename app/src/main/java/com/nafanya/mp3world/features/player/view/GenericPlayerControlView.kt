package com.nafanya.mp3world.features.player.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.util.RepeatModeUtil
import com.google.android.material.imageview.ShapeableImageView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.domain.Song
import com.nafanya.mp3world.features.foregroundService.PlayerLiveDataProvider

open class GenericPlayerControlView(
    private val activity: AppCompatActivity,
    layoutResId: Int
) {
    // id in activity xml
    var playerControlView: StyledPlayerControlView = activity.findViewById(layoutResId)
    private lateinit var song: Song

    init {
        // setting view
        playerControlView.showTimeoutMs = 0
        playerControlView.isNestedScrollingEnabled = false
        // setting player
        val playerObserver = Observer<Boolean> {
            if (it) {
                playerControlView.player = PlayerLiveDataProvider.getPlayer()
            }
        }
        PlayerLiveDataProvider.isPlayerInitialized.observe(activity, playerObserver)
        playerControlView.repeatToggleModes =
            RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL or
            RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE or
            RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE
        if (activity !is FullScreenPlayerActivity) {
            activity.findViewById<LinearLayout>(R.id.controls_view).setOnClickListener {
                this.toFullScreen()
            }
        }
    }

    open fun setSongObserver() {
        // observe current song state
        val songObserver = Observer<Song> {
            song = it
            activity.findViewById<LinearLayout>(R.id.controls_view).visibility = View.VISIBLE
            activity.findViewById<TextView>(R.id.track_title).text = song.title
            activity.findViewById<TextView>(R.id.track_artist).text = song.artist
            setSongIcon()
        }
        PlayerLiveDataProvider.currentSong.observe(activity, songObserver)
    }

    private fun setSongIcon() {
        val songIcon = activity.findViewById<ImageView>(R.id.control_song_icon)
        when {
            song.art != null -> {
                songIcon.setImageBitmap(song.art)
            }
            song.artUrl != null -> {
                Glide.with(songIcon)
                    .asBitmap()
                    .load(song.artUrl)
                    .into(songIcon)
            }
            else -> {
                songIcon.setImageResource(R.drawable.music_menu_icon)
            }
        }
    }

    fun toFullScreen() {
        var bundle: Bundle? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                activity,
                Pair.create(
                    activity.findViewById<ShapeableImageView>(R.id.control_song_icon),
                    activity.getString(R.string.player_transition)
                )
            )
            bundle = options.toBundle()
        }

        val intent = Intent(activity, FullScreenPlayerActivity::class.java)
        startActivity(activity, intent, bundle)
        setSongIcon()
    }
}
