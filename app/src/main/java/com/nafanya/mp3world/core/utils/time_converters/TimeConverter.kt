package com.nafanya.mp3world.core.utils.time_converters

/**
 * Class that converts duration to string and string to duration.
 */
object TimeConverter {

    private const val MILLISECONDS_IN_ONE_HOUR = 3_600_000
    private const val MILLISECONDS_IN_ONE_MINUTES = 60_000
    private const val MILLISECONDS_IN_ONE_SECOND = 1_000

    fun stringToDuration(value: String): Long {
        var result = 0L
        val split = value.split(':').reversed() as ArrayList<String>
        result += (split[0].toInt()) * MILLISECONDS_IN_ONE_SECOND
        if (split.size > 1) result += (split[1].toInt()) * MILLISECONDS_IN_ONE_MINUTES
        if (split.size > 2) result += (split[2].toInt()) * MILLISECONDS_IN_ONE_HOUR
        return result
    }

    fun durationToString(value: Long): String {
        var duration = value
        var hours = 0
        while (duration >= MILLISECONDS_IN_ONE_HOUR) {
            hours++
            duration -= MILLISECONDS_IN_ONE_HOUR
        }
        var minutes = 0
        while (duration >= MILLISECONDS_IN_ONE_MINUTES) {
            minutes++
            duration -= MILLISECONDS_IN_ONE_MINUTES
        }
        var seconds = 0
        while (duration >= MILLISECONDS_IN_ONE_SECOND) {
            seconds++
            duration -= MILLISECONDS_IN_ONE_SECOND
        }
        return if (hours == 0) {
            minutes.toString() + ":" + seconds.toString().padStart(2, '0')
        } else {
            hours.toString() + ":" +
                minutes.toString().padStart(2, '0') + ":" +
                seconds.toString().padStart(2, '0')
        }
    }
}
