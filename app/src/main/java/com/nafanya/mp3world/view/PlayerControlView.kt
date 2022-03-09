package com.nafanya.mp3world.view

import android.content.Context
import android.view.MotionEvent
import com.google.android.exoplayer2.ui.StyledPlayerControlView

class PlayerControlView(context: Context) : StyledPlayerControlView(context) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        performClick()
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}