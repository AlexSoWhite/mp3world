package com.nafanya.mp3world.model.network

enum class ResultType {
    SUCCESS,
    ERROR
}

data class DownloadResult (
    val type: ResultType,
)