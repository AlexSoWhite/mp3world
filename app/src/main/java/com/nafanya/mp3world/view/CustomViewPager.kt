package com.nafanya.mp3world.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.nafanya.mp3world.viewmodel.PagerStateController

class CustomViewPager : ViewPager {

    constructor(context: Context) : super(context) {
        offscreenPageLimit = keepPages
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        offscreenPageLimit = keepPages
    }

    private var isPagingEnabled = true

    fun subscribePaging(lifecycleOwner: LifecycleOwner) {
        val observer = Observer<Boolean> {
            isPagingEnabled = it
        }
        PagerStateController.isPagingEnabled.observe(lifecycleOwner, observer)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return this.isPagingEnabled && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return this.isPagingEnabled && super.onInterceptTouchEvent(ev)
    }

    companion object {
        private const val keepPages = 10
    }
}
