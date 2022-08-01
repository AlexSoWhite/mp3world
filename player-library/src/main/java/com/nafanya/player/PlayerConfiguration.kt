package com.nafanya.player

data class PlayerConfiguration(
    val durationDecreaseVolume: Long,
    val durationIncreaseVolume: Long
) {

    companion object {

        const val defaultDurationDecreaseVolume = 500L
        const val defaultDurationIncreaseVolume = 500L
    }
}

val DefaultConfiguration = PlayerConfiguration(
    PlayerConfiguration.defaultDurationDecreaseVolume,
    PlayerConfiguration.defaultDurationIncreaseVolume
)
