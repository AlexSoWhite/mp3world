package com.nafanya.mp3world.view

import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class OnSwipeListener(view: View, var callback: () -> Unit) : View.OnTouchListener {

    private var lastX: Float = 0F
    private var lastY: Float = 0F
    private var lastIsUp = true

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        val result = false
        view?.performClick()
        event?.let {
            if (event.action == MotionEvent.ACTION_UP) {
                val diffX = lastX - event.x
                val diffY = lastY - event.y
                // Log.d("touch", "$diffX $diffY")
                if (abs(diffX) > 2 * abs(diffY)) {
                    Log.d("touch", "swipe detected")
                    callback()
                }
                lastIsUp = true
            } else if (lastIsUp) {
                lastX = event.x
                lastY = event.y
                lastIsUp = false
            }
            // Log.d("touch", MotionEvent.actionToString(event.action))
        }
        return result
    }

    init {
        view.setOnTouchListener(this)
    }
}
