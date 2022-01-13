package com.nafanya.mp3world.model

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class OnSwipeTouchListener internal constructor(
    ctx: Context,
    mainView: View,
    private val callback: () -> Unit
) : View.OnTouchListener {

    private val gestureDetector: GestureDetector
    private var context: Context

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            val diffY = e2.y - e1.y
            val diffX = e2.x - e1.x
            if (abs(diffX) > abs(diffY) &&
                abs(diffX) > SWIPE_THRESHOLD &&
                abs(velocityX) > SWIPE_VELOCITY_THRESHOLD
            ) {
                if (diffX > 0) {
                    onSwipeRight()
                } else {
                    onSwipeLeft()
                }
                result = true
            } else if (
                abs(diffY) > SWIPE_THRESHOLD &&
                abs(velocityY) > SWIPE_VELOCITY_THRESHOLD
            ) {
                if (diffY > 0) {
                    onSwipeBottom()
                } else {
                    onSwipeTop()
                }
                result = true
            }
            return result
        }
    }

    companion object {
        private const val SWIPE_THRESHOLD = 200
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }

    fun onSwipeRight() {
        // Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show()
        callback()
        onSwipe!!.swipeRight()
    }

    fun onSwipeLeft() {
        // Toast.makeText(context, "Swiped Left", Toast.LENGTH_SHORT).show()
        callback()
        onSwipe!!.swipeLeft()
    }

    fun onSwipeTop() {
        // Toast.makeText(context, "Swiped Up", Toast.LENGTH_SHORT).show()
        onSwipe!!.swipeTop()
    }

    fun onSwipeBottom() {
        // Toast.makeText(context, "Swiped Down", Toast.LENGTH_SHORT).show()
        onSwipe!!.swipeBottom()
    }

    interface OnSwipeListener {
        fun swipeRight()
        fun swipeTop()
        fun swipeBottom()
        fun swipeLeft()
    }

    var onSwipe: OnSwipeListener? = null

    init {
        gestureDetector = GestureDetector(ctx, GestureListener())
        mainView.setOnTouchListener(this)
        context = ctx
    }
}
