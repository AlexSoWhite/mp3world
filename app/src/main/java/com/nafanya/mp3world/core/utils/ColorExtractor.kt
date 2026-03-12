package com.nafanya.mp3world.core.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.nafanya.mp3world.core.coroutines.DispatchersProvider
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import androidx.core.graphics.get

class ColorExtractor @Inject constructor(
    private val dispatchersProvider: DispatchersProvider
) {

    companion object {
        private const val COLOR_TAKING_MAX_THRESHOLD = 0.8f
        private const val COLOR_TAKING_MIN_THRESHOLD = 0.05f

        private const val TAG = "_ColorExtractor"
    }

    private class Chunk(
        val count: Long,
        val accumulatedRed: Double,
        val accumulatedGreen: Double,
        val accumulatedBlue: Double
    ) {
        fun getColor(): Int {
            return Color.valueOf(
                (accumulatedRed / count).toFloat(),
                (accumulatedGreen / count).toFloat(),
                (accumulatedBlue / count).toFloat()
            ).toArgb()
        }
    }

    @OptIn(ExperimentalTime::class)
    @RequiresApi(Build.VERSION_CODES.Q)
    @Suppress("ComplexCondition")
    @Deprecated(
        "Use new method, this one is kept just for performance demonstrations",
        replaceWith = ReplaceWith("getAverageColorWithNoWhiteComponent")
    )
    suspend fun getAverageColorWithNoWhiteComponentLegacy(bitmap: Bitmap): Int {
        val (result, time) = measureTimedValue {
            withContext(dispatchersProvider.default) {
                var accumulatedRed = 0.0
                var accumulatedGreen = 0.0
                var accumulatedBlue = 0.0
                val width = bitmap.width
                val height = bitmap.height
                var count: Long = 0
                val thresholdRange = COLOR_TAKING_MIN_THRESHOLD .. COLOR_TAKING_MAX_THRESHOLD
                for (i in 0 until width) {
                    for (j in 0 until height) {
                        val pixel = bitmap[i, j]
                        val redComponent = Color.red(pixel) / 255.toFloat()
                        val greenComponent = Color.green(pixel) / 255.toFloat()
                        val blueComponent = Color.blue(pixel) / 255.toFloat()
                        if (
                            redComponent in thresholdRange &&
                            greenComponent in thresholdRange &&
                            blueComponent in thresholdRange
                        ) {
                            count++
                            accumulatedRed += redComponent
                            accumulatedGreen += greenComponent
                            accumulatedBlue += blueComponent
                        }
                    }
                }
                accumulatedRed /= count
                accumulatedGreen /= count
                accumulatedBlue /= count
                return@withContext Color.valueOf(
                    accumulatedRed.toFloat(),
                    accumulatedGreen.toFloat(),
                    accumulatedBlue.toFloat()
                ).toArgb()
            }
        }
        Log.d(TAG, "time taken: ${time.inWholeMilliseconds} ms")
        return result
    }

    @OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
    @Suppress("ComplexCondition")
    suspend fun getAverageColorWithNoWhiteComponent(bitmap: Bitmap): Int {
        val (result, time) = measureTimedValue {
            val cpus = Runtime.getRuntime().availableProcessors()
            withContext(dispatchersProvider.default.limitedParallelism(cpus)) {
                val width = bitmap.width
                val height = bitmap.height
                val cpus = Runtime.getRuntime().availableProcessors()

                val widthRanges = getRanges(width, cpus)
                val heightRanges = getRanges(height, cpus)

                val deferredList = mutableListOf<Deferred<Chunk>>()

                for (i in 0 .. cpus) {
                    deferredList.add(
                        async {
                            calculateChunk(bitmap, widthRanges[i], heightRanges[i])
                        }
                    )
                }

                val bigChunk = deferredList.awaitAll().reduce { acc, chunk ->
                    Chunk(
                        count = acc.count + chunk.count,
                        accumulatedRed = acc.accumulatedRed + chunk.accumulatedRed,
                        accumulatedGreen = acc.accumulatedGreen + chunk.accumulatedGreen,
                        accumulatedBlue = acc.accumulatedBlue + chunk.accumulatedBlue
                    )
                }

                return@withContext bigChunk.getColor()
            }
        }
        Log.d(TAG, "time taken: ${time.inWholeMilliseconds} ms")
        return result
    }

    private fun getRanges(value: Int, count: Int): List<IntRange> {
        val step = value.floorDiv(count)
        val ranges = mutableListOf<IntRange>()
        var start = 0
        while (start <= value) {
            // Use Long to avoid overflow when calculating the end
            val end = (start + step).coerceAtMost(value - 1)
            ranges.add(start .. end)
            // Move to next start; overflow is not an issue because the loop condition
            // will be false once start exceeds max.
            start += step
        }
        return ranges
    }

    private suspend fun calculateChunk(bitmap: Bitmap, widthRange: IntRange, heightRange: IntRange): Chunk = withContext(dispatchersProvider.default) {
        var accumulatedRed = 0.0
        var accumulatedGreen = 0.0
        var accumulatedBlue = 0.0
        var count: Long = 0
        val thresholdRange = COLOR_TAKING_MIN_THRESHOLD .. COLOR_TAKING_MAX_THRESHOLD
        for (i in widthRange) {
            for (j in heightRange) {
                val pixel = bitmap[i, j]
                val redComponent = Color.red(pixel) / 255.toFloat()
                val greenComponent = Color.green(pixel) / 255.toFloat()
                val blueComponent = Color.blue(pixel) / 255.toFloat()
                if (
                    redComponent in thresholdRange &&
                    greenComponent in thresholdRange &&
                    blueComponent in thresholdRange
                ) {
                    count++
                    accumulatedRed += redComponent
                    accumulatedGreen += greenComponent
                    accumulatedBlue += blueComponent
                }
            }
        }
        return@withContext Chunk(
            count = count,
            accumulatedRed = accumulatedRed,
            accumulatedGreen = accumulatedGreen,
            accumulatedBlue = accumulatedBlue
        )
    }
}
