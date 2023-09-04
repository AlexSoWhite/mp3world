package com.nafanya.mp3world.features.downloading

import android.content.Context
import android.os.Environment
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import javax.inject.Inject

/**
 * Class that downloads song by [android.app.DownloadManager].
 * May be replaced with SoundCloud API call when it opens.
 */
@Suppress("TooGenericExceptionCaught")
class Downloader @Inject constructor(
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
        callback: (DownloadResult) -> Unit
    ) {
        val url = song.uri.toString()
        val fileName = "${song.artist} - ${song.title}.mp3"
        try {
            downloadManagerInteractor.downloadFromUrl(url, fileName).collect { name ->
                if (name!!.isNotEmpty()) {
                    scan(name, callback)
                }
            }
        } catch (exception: Exception) {
            callback(DownloadResult(ResultType.ERROR))
        }
    }

    /**
     * Use [MetadataScanner] to submit automatically all media metadata to downloaded file
     */
    private fun scan(name: String, callback: (DownloadResult) -> Unit) {
        MetadataScanner(context, "$DOWNLOAD_DIR/$name") {
            when (it) {
                ScannerResult.SUCCESS -> {
                    callback(DownloadResult(ResultType.SUCCESS))
                }
                ScannerResult.FAILED -> {
                    callback(DownloadResult(ResultType.ERROR))
                }
            }
        }
    }
}
