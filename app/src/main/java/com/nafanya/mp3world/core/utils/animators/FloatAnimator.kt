package com.nafanya.mp3world.core.utils.animators

import android.animation.ValueAnimator

class FloatAnimator(
    private val updateListener: (Float) -> Unit,
    private val duration: Long = DEFAULT_DURATION
) {

    companion object {
        const val DEFAULT_DURATION = 200L
    }

    private val animatorIncrease: ValueAnimator by lazy { ValueAnimator.ofFloat(0.0f, 1.0f) }
    private val animatorDecrease: ValueAnimator by lazy { ValueAnimator.ofFloat(1.0f, 0.0f) }

    fun startIncrease() {
        animatorIncrease.addUpdateListener {
            updateListener(it.animatedValue as Float)
        }
        animatorIncrease.start()
    }

    fun startDecrease() {
        animatorDecrease.addUpdateListener {
            updateListener(it.animatedValue as Float)
        }
        animatorDecrease.start()
    }
}
