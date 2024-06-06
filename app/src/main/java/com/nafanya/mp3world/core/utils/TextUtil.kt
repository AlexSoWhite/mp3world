package com.nafanya.mp3world.core.utils

@Suppress("MagicNumber")
/**
 * TODO string res
 */
object TextUtil {

    fun getCompositionsCountString(amount: Int): String {
        return when (amount % 10) {
            0 -> "$amount композиций"
            1 -> "$amount композиция"
            2 -> "$amount композиции"
            3 -> "$amount композиции"
            4 -> "$amount композиции"
            5 -> "$amount композиций"
            6 -> "$amount композиций"
            7 -> "$amount композиций"
            8 -> "$amount композиций"
            9 -> "$amount композиций"
            else -> ""
        }
    }
}
