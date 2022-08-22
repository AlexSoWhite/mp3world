package com.nafanya.player

data class PlayerConfiguration(
    val durationDecreaseVolume: Long,
    val durationIncreaseVolume: Long
) {

    companion object {

        const val defaultDurationDecreaseVolume = 200L
        const val defaultDurationIncreaseVolume = 200L
    }
}

val DefaultConfiguration = PlayerConfiguration(
    PlayerConfiguration.defaultDurationDecreaseVolume,
    PlayerConfiguration.defaultDurationIncreaseVolume
)
