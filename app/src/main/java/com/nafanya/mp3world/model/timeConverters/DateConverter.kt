package com.nafanya.mp3world.model.timeConverters

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Class that converts timestamp to date.
 */
class DateConverter {

    companion object {
        private const val multiplier = 1000L
    }

    fun dateToString(arg: Long): String {
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))
        return simpleDateFormat.format(arg * multiplier)
    }
}
