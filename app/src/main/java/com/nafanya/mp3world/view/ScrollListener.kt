package com.nafanya.mp3world.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.transition.doOnEnd
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.viewmodel.PagerStateController

class ScrollListener(
    private val activity: FragmentActivity,
    private val shrinkableRecyclerView: RecyclerView,
    private val alphaAnimated: Drawable? = null
) : RecyclerView.OnScrollListener() {

    private var isFullScreen = false
    private var rememberedHeight: Int = 0

    init {
        rememberedHeight = shrinkableRecyclerView.layoutParams.height
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (!isFullScreen && dy > 0) {
            PagerStateController.isPagingEnabled.value = false
            val layoutParams = recyclerView.layoutParams
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            recyclerView.layoutParams = layoutParams
            val transition = AutoTransition()
            transition.duration = animationDuration
            transition.doOnEnd {
                isFullScreen = true
            }
            val alphaAnimation = ValueAnimator.ofObject(ArgbEvaluator(), maxAlpha, 0)
            alphaAnimation.addUpdateListener {
                alphaAnimated?.alpha = it.animatedValue as Int
            }
            alphaAnimation.start()
            val mainView = activity.findViewById<ConstraintLayout>(R.id.recyclers_container)
            TransitionManager.beginDelayedTransition(mainView, transition)
        }
    }

    fun shrink() {
        if (isFullScreen) {
            val layoutParams = shrinkableRecyclerView.layoutParams
            layoutParams.height = rememberedHeight
            shrinkableRecyclerView.layoutParams = layoutParams
            val transition = AutoTransition()
            transition.duration = animationDuration
            transition.doOnEnd {
                isFullScreen = false
                PagerStateController.isPagingEnabled.value = true
            }
            val alphaAnimation = ValueAnimator.ofObject(ArgbEvaluator(), 0, maxAlpha)
            alphaAnimation.addUpdateListener {
                alphaAnimated?.alpha = it.animatedValue as Int
            }
            alphaAnimation.start()
            val mainView = activity.findViewById<ConstraintLayout>(R.id.recyclers_container)
            TransitionManager.beginDelayedTransition(mainView, transition)
        }
    }

    companion object {
        private const val animationDuration = 300L
        private const val maxAlpha = 255
    }
}
