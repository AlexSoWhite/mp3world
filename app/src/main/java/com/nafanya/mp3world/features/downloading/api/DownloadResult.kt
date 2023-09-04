package com.nafanya.mp3world.features.downloading.api

enum class ResultType {
    SUCCESS,
    ERROR
}

data class DownloadResult(
    val type: ResultType,
)
