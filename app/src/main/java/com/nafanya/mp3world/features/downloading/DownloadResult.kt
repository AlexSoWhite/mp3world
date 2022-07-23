package com.nafanya.mp3world.features.downloading

enum class ResultType {
    SUCCESS,
    ERROR
}

data class DownloadResult(
    val type: ResultType,
)
