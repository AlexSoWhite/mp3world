package com.nafanya.mp3world.core.utils.timeConverters

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Class that converts timestamp to date.
 */
object DateConverter {

    private const val multiplier = 1000L
    private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))

    fun dateToString(arg: Long): String {
        return simpleDateFormat.format(arg * multiplier)
    }
}
