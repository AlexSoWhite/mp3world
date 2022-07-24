package com.nafanya.mp3world.features.playlists.playlist.view

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
                if (abs(diffX) > 2 * abs(diffY) && diffX < 0) {
                    Log.d("touch", "right swipe detected")
                    callback()
                }
                lastIsUp = true
            } else if (lastIsUp) {
                lastX = event.x
                lastY = event.y
                lastIsUp = false
            }
        }
        return result
    }

    init {
        view.setOnTouchListener(this)
    }
}
