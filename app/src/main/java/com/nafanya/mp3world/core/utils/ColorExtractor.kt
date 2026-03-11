package com.nafanya.mp3world.core.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.nafanya.mp3world.core.coroutines.DispatchersProvider
import javax.inject.Inject
import kotlinx.coroutines.withContext

class ColorExtractor @Inject constructor(
    private val dispatchersProvider: DispatchersProvider
) {

    companion object {
        private const val COLOR_TAKING_MAX_THRESHOLD = 0.8f
        private const val COLOR_TAKING_MIN_THRESHOLD = 0.05f
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Suppress("ComplexCondition")
    suspend fun getAverageColorWithNoWhiteComponent(bitmap: Bitmap): Int = withContext(dispatchersProvider.default) {
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
                    color.red() < COLOR_TAKING_MAX_THRESHOLD &&
                    color.green() < COLOR_TAKING_MAX_THRESHOLD &&
                    color.blue() < COLOR_TAKING_MAX_THRESHOLD &&
                    color.red() > COLOR_TAKING_MIN_THRESHOLD &&
                    color.green() > COLOR_TAKING_MIN_THRESHOLD &&
                    color.blue() > COLOR_TAKING_MIN_THRESHOLD
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
        return@withContext Color.valueOf(
            averageRed.toFloat(),
            averageGreen.toFloat(),
            averageBlue.toFloat()
        ).toArgb()
    }
}
