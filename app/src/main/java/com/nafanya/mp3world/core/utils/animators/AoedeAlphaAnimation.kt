package com.nafanya.mp3world.core.utils.animators

import android.view.animation.AlphaAnimation

class AoedeAlphaAnimation(
    startValue: Float = DEFAULT_START_VALUE,
    endValue: Float = DEFAULT_END_VALUE,
    duration: Long = DEFAULT_DURATION,
    startOffset: Long = DEFAULT_START_OFFSET
) : AlphaAnimation(startValue, endValue) {

    companion object {
        const val DEFAULT_START_OFFSET = 350L
        const val DEFAULT_DURATION = 500L
        const val DEFAULT_START_VALUE = 0.0f
        const val DEFAULT_END_VALUE = 1.0f
    }

    init {
        this.startOffset = startOffset
        this.duration = duration
    }
}
