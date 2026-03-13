package com.nafanya.mp3world.core.utils.time_converters

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Class that converts timestamp to date.
 */
object DateConverter {

    private const val MILLISECONDS_IN_ONE_SECOND = 1000L
    private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))

    fun millisecondsToDateString(arg: Long): String {
        return simpleDateFormat.format(arg * MILLISECONDS_IN_ONE_SECOND)
    }
}
