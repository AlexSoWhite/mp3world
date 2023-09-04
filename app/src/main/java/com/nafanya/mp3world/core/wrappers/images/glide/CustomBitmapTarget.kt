package com.nafanya.mp3world.core.wrappers.images.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class CustomBitmapTarget(
    private val onLoaded: (Bitmap) -> Unit,
    private val onError: (() -> Unit)? = null
) : CustomTarget<Bitmap>() {

    override fun onLoadCleared(placeholder: Drawable?) {
        onError?.invoke()
    }

    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        onLoaded.invoke(resource)
    }
}
