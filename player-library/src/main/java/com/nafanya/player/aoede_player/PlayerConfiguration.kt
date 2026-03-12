package com.nafanya.player.aoede_player

data class PlayerConfiguration(
    val durationDecreaseVolume: Long,
    val durationIncreaseVolume: Long
) {

    companion object {

        const val DEFAULT_DURATION_DECREASE_VOLUME = 200L
        const val DEFAULT_DURATION_INCREASE_VOLUME = 200L
    }
}

val DefaultConfiguration = PlayerConfiguration(
    PlayerConfiguration.DEFAULT_DURATION_DECREASE_VOLUME,
    PlayerConfiguration.DEFAULT_DURATION_INCREASE_VOLUME
)
