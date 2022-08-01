package com.nafanya.player

import android.animation.ValueAnimator
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ForwardingPlayer
import com.google.android.exoplayer2.Player

class SmoothlyPausablePlayer(
    val exoPlayer: ExoPlayer,
    val configuration: PlayerConfiguration = DefaultConfiguration
) : ForwardingPlayer(exoPlayer) {

    override fun pause() {
        exoPlayer.smoothlyDecreaseVolumeAndPause()
    }

    override fun play() {
        exoPlayer.playAndSmoothlyIncreaseVolume()
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
