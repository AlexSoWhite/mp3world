package com.nafanya.mp3world.core.utils

import android.content.res.Resources

fun Int.dpToPx(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
