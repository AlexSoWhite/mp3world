package com.nafanya.mp3world.view

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

object ColorExtractor {

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getAverageColor(bitmap: Bitmap): Color {
        var averageRed = 0.0F
        var averageGreen = 0.0F
        var averageBlue = 0.0F
        val width = bitmap.width
        val height = bitmap.height
        for (i in 0 until width) {
            for (j in 0 until height) {
                val color = bitmap.getColor(i, j)
                averageRed += color.red() / (width * height)
                averageGreen += color.green() / (width * height)
                averageBlue += color.blue() / (width * height)
            }
        }
        return Color.valueOf(averageRed, averageGreen, averageBlue)
    }
}
