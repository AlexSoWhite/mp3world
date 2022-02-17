package com.nafanya.mp3world.view.legacy

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.databinding.LegacyRecyclerHolderFragmentBinding
import com.nafanya.mp3world.viewmodel.legacy.PagerStateController

class ScrollListener(
    private val scale: Float,
    private val binding: LegacyRecyclerHolderFragmentBinding
) : RecyclerView.OnScrollListener() {

    private var isFullScreen = false
    private var isResizing = false
    private var rememberedLayoutParams = binding.recycler.layoutParams

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (!isResizing && !isFullScreen && dy > 0) {
            PagerStateController.isPagingEnabled.value = false
            isResizing = true
            // resize animation
            val paddingAnimation = ValueAnimator.ofObject(
                ArgbEvaluator(),
                previewRawVerticalPadding,
                fullRawVerticalPadding
            )
            paddingAnimation.duration = animationDuration
            paddingAnimation.addUpdateListener {
                binding.list.setPadding(
                    dp(rawHorizontalPadding),
                    dp(it.animatedValue as Int),
                    dp(rawHorizontalPadding),
                    dp(it.animatedValue as Int)
                )
            }
            paddingAnimation.doOnEnd {
                isFullScreen = true
                isResizing = false
            }
            // background animation
            val alphaAnimation = ValueAnimator.ofObject(ArgbEvaluator(), maxAlpha, 0)
            alphaAnimation.duration = animationDuration
            alphaAnimation.addUpdateListener {
                binding.recycler.background.alpha = it.animatedValue as Int
            }
            paddingAnimation.start()
            alphaAnimation.start()
        }
    }

    fun shrink() {
        if (isFullScreen && !isResizing) {
            binding.recycler.layoutParams = rememberedLayoutParams
            isResizing = true
            // resize animation
            val paddingAnimation = ValueAnimator.ofObject(
                ArgbEvaluator(),
                fullRawVerticalPadding,
                previewRawVerticalPadding
            )
            paddingAnimation.duration = animationDuration
            paddingAnimation.addUpdateListener {
                binding.list.setPadding(
                    dp(rawHorizontalPadding),
                    dp(it.animatedValue as Int),
                    dp(rawHorizontalPadding),
                    dp(it.animatedValue as Int)
                )
            }
            paddingAnimation.doOnEnd {
                isFullScreen = false
                isResizing = false
                PagerStateController.isPagingEnabled.value = true
            }
            // background animation
            val alphaAnimation = ValueAnimator.ofObject(ArgbEvaluator(), 0, maxAlpha)
            alphaAnimation.addUpdateListener {
                binding.recycler.background.alpha = it.animatedValue as Int
            }
            paddingAnimation.start()
            alphaAnimation.start()
        }
    }

    private fun dp(arg: Int): Int {
        return (arg * scale + scaleAdjuster).toInt()
    }

    companion object {
        private const val animationDuration = 300L
        private const val maxAlpha = 255
        private const val rawHorizontalPadding = 30
        private const val previewRawVerticalPadding = 200
        private const val fullRawVerticalPadding = 100
        private const val scaleAdjuster = 0.5F
    }
}
