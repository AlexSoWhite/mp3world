package com.nafanya.mp3world.view

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

object ColorExtractor {

    private const val colorTakingThreshold = 2.4f

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getAverageColorWithNoWhiteComponent(bitmap: Bitmap): Int {
        var averageRed: Double = 0.0
        var averageGreen: Double = 0.0
        var averageBlue: Double = 0.0
        val width = bitmap.width
        val height = bitmap.height
        var count: Long = 0
        for (i in 0 until width) {
            for (j in 0 until height) {
                val color = bitmap.getColor(i, j)
                if (color.red() + color.green() + color.blue() < colorTakingThreshold) {
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
