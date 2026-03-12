package com.nafanya.player.aoede_player

import android.animation.ValueAnimator
import android.util.Log
import androidx.media3.common.ForwardingPlayer
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

@UnstableApi
internal class ExoPlayerWrapper(
    val exoPlayer: ExoPlayer,
    private val configuration: PlayerConfiguration = DefaultConfiguration
) : ForwardingPlayer(exoPlayer) {

    private companion object {
        const val TAG = "_ExoPlayerWrapper"
    }

    override fun pause() {
        Log.d(TAG, "pause")
        exoPlayer.smoothlyDecreaseVolumeAndPause()
    }

    override fun play() {
        Log.d(TAG, "play")
        exoPlayer.playAndSmoothlyIncreaseVolume()
    }

    fun toggle() {
        Log.d(TAG, "toggle")
        if (exoPlayer.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    private fun Player.playAndSmoothlyIncreaseVolume() {
        val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
        animator.addUpdateListener {
            this.volume = it.animatedValue as Float
        }
        animator.duration = configuration.durationIncreaseVolume
        animator.start()
        super.play()
    }

    private fun Player.smoothlyDecreaseVolumeAndPause() {
        val animator = ValueAnimator.ofFloat(1.0f, 0.0f)
        animator.addUpdateListener {
            this.volume = it.animatedValue as Float
            if (this.volume == 0.0f) {
                super.pause()
            }
        }
        animator.duration = configuration.durationDecreaseVolume
        animator.start()
    }
}
