package com.nafanya.mp3world.model.network

import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.NotificationCompat
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.nafanya.mp3world.R
import com.nafanya.mp3world.model.dependencies.PlayerApplication
import com.nafanya.mp3world.model.wrappers.Song
import java.io.File

/**
 * Class that downloads song by PRDownloader.
 */
@Suppress("TooGenericExceptionCaught")
class Downloader {

    private val context = PlayerApplication.context()
    private val builder = NotificationCompat.Builder(context, "download")
    private val notifyManager: NotificationManager =
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    private var id = 0

    companion object {
        private var lastId = 100
        private const val multiplicator = 100
        private val DOWNLOAD_DIR = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .absolutePath
    }

    fun downLoad(song: Song, callback: (DownloadResult) -> Unit) {
        builder
            .setContentTitle("${song.artist} - ${song.title}")
            .setContentText("загрузка")
            .setSmallIcon(R.drawable.downloading_icon)
        id = lastId
        lastId++
        downLoadL(song, callback)
    }

    private fun downLoadL(song: Song, callback: (DownloadResult) -> Unit): Int {
        val url = song.url!!
        var fileName = url
            .substring(url.lastIndexOf('/') + 1)
            .replace('_', ' ')
        fileName = fileName.substring(0, fileName.lastIndexOf(' ')) + ".mp3"
        val dirPath = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            context.filesDir.absolutePath
        } else {
            DOWNLOAD_DIR
        }
        return PRDownloader.download(url, dirPath, fileName)
            .build()
            .setOnProgressListener {
                val progressPercent = it.currentBytes * multiplicator / it.totalBytes
                Log.d("Download", "progress: ${progressPercent.toInt()}")
                builder.setProgress(multiplicator, progressPercent.toInt(), false)
                notifyManager.notify(
                    id,
                    builder.build()
                )
            }
            .start(
                object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        try {
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                                copyFileToDownloads(fileName)
                            }
                            MetadataScanner("$DOWNLOAD_DIR/$fileName")
                            callback(DownloadResult(ResultType.SUCCESS))
                        } catch (exception: Exception) {
                            callback(DownloadResult(ResultType.ERROR))
                        }
                    }

                    override fun onError(error: Error?) {
                        Log.d("Download", error.toString())
                        callback(DownloadResult(ResultType.ERROR))
                    }
                }
            )
    }

    fun copyFileToDownloads(filename: String) {
        val src = File(context.filesDir, filename)
        val dst = File(DOWNLOAD_DIR, filename)
        src.copyTo(dst)
        src.delete()
    }

    private fun File.copyTo(file: File) {
        inputStream().use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}
