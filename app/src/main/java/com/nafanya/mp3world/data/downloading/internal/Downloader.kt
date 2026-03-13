package com.nafanya.mp3world.data.downloading.internal

import android.content.Context
import android.os.Environment
import com.nafanya.mp3world.core.wrappers.song.joinArtists
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.data.downloading.api.DownloadResult
import com.nafanya.mp3world.data.downloading.api.ResultType
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Class that downloads song by [android.app.DownloadManager].
 * May be replaced with SoundCloud API call when it opens.
 */
@Suppress("TooGenericExceptionCaught")
internal class Downloader(
    private val context: Context,
    private val downloadManagerInteractor: DownloadManagerInteractor
) {

    companion object {
        private val DOWNLOAD_DIR = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .absolutePath
    }

    suspend fun download(
        song: RemoteSong,
    ): DownloadResult {
        val url = song.uri.toString()
        val fileName = "${song.artists.joinArtists()} - ${song.title}.mp3"
        return try {
            val name = downloadManagerInteractor.downloadFromUrl(url, fileName)
            if (name!!.isNotEmpty()) {
                scan(name)
            } else {
                DownloadResult(ResultType.ERROR) // todo: we probably never get here
            }
        } catch (exception: Exception) {
            DownloadResult(ResultType.ERROR)
        }
    }

    /**
     * Use [MetadataScanner] to submit automatically all media metadata to downloaded file.
     */
    private suspend fun scan(name: String): DownloadResult {
        return suspendCancellableCoroutine { continuation ->
            MetadataScanner(context, "$DOWNLOAD_DIR/$name") {
                when (it) {
                    ScannerResult.SUCCESS -> {
                        continuation.resume(DownloadResult(ResultType.SUCCESS))
                    }

                    ScannerResult.FAILED -> {
                        continuation.resume(DownloadResult(ResultType.ERROR))
                    }
                }
            }
        }
    }
}
