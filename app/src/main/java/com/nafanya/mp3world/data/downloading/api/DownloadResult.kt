package com.nafanya.mp3world.data.downloading.api

enum class ResultType {
    SUCCESS,
    ERROR
}

data class DownloadResult(
    val type: ResultType,
)
