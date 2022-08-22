package com.nafanya.mp3world.core.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

object ColorExtractor {

    private const val colorTakingMaxThreshold = 0.8f
    private const val colorTakingMinThreshold = 0.05f

    @RequiresApi(Build.VERSION_CODES.Q)
    @Suppress("ComplexCondition")
    fun getAverageColorWithNoWhiteComponent(bitmap: Bitmap): Int {
        var averageRed = 0.0
        var averageGreen = 0.0
        var averageBlue = 0.0
        val width = bitmap.width
        val height = bitmap.height
        var count: Long = 0
        for (i in 0 until width) {
            for (j in 0 until height) {
                val color = bitmap.getColor(i, j)
                if (
                    color.red() < colorTakingMaxThreshold &&
                    color.green() < colorTakingMaxThreshold &&
                    color.blue() < colorTakingMaxThreshold &&
                    color.red() > colorTakingMinThreshold &&
                    color.green() > colorTakingMinThreshold &&
                    color.blue() > colorTakingMinThreshold
                ) {
                    count++
                    averageRed += color.red()
                    averageGreen += color.green()
                    averageBlue += color.blue()
                }
            }
        }
        averageRed /= count
        averageGreen /= count
        averageBlue /= count
        return Color.valueOf(
            averageRed.toFloat(),
            averageGreen.toFloat(),
            averageBlue.toFloat()
        ).toArgb()
    }
}
