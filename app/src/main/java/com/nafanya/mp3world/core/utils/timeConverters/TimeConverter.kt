package com.nafanya.mp3world.core.utils.timeConverters

/**
 * Class that converts duration to string and string to duration.
 */
class TimeConverter {

    fun stringToDuration(value: String): Long {
        var result = 0L
        val split = value.split(':').reversed() as ArrayList<String>
        result += (split[0].toInt()) * millisecondsInOneSecond
        if (split.size > 1) result += (split[1].toInt()) * millisecondsInOneMinute
        if (split.size > 2) result += (split[2].toInt()) * millisecondsInOneHour
        return result
    }

    fun durationToString(value: Long): String {
        var duration = value
        var hours = 0
        while (duration >= millisecondsInOneHour) {
            hours++
            duration -= millisecondsInOneHour
        }
        var minutes = 0
        while (duration >= millisecondsInOneMinute) {
            minutes++
            duration -= millisecondsInOneMinute
        }
        var seconds = 0
        while (duration >= millisecondsInOneSecond) {
            seconds++
            duration -= millisecondsInOneSecond
        }
        return if (hours == 0) {
            minutes.toString() + ":" + seconds.toString().padStart(2, '0')
        } else {
            hours.toString() + ":" +
                minutes.toString().padStart(2, '0') + ":" +
                seconds.toString().padStart(2, '0')
        }
    }

    companion object {
        private const val millisecondsInOneHour = 3600000
        private const val millisecondsInOneMinute = 60000
        private const val millisecondsInOneSecond = 1000
    }
}
