package com.nafanya.mp3world.model.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.nafanya.mp3world.R
import com.nafanya.mp3world.model.listManagers.MediaStoreReader
import com.nafanya.mp3world.model.wrappers.Song
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

/**
 * Class that downloads song by PRDownloader.
 * Will be replaced with SoundCloud API call when it opens.
 * TODO: API 29
 */
@Suppress("TooGenericExceptionCaught")
class Downloader(
    private var context: Context
) {

    private val builder = NotificationCompat.Builder(context, "download")
    private val mediaStoreReader = MediaStoreReader.Builder().withContext(context).build()
    private val notificationManager =
        getSystemService(context, NotificationManager::class.java) as NotificationManager

    private var id = 0

    companion object {
        private var lastId = 100
        private const val multiplier = 100
        private var isChannelCreated = false
        private val DOWNLOAD_DIR = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .absolutePath
    }

    fun download(
        song: Song,
        callback: (DownloadResult) -> Unit
    ) {
        builder
            .setContentTitle("${song.artist} - ${song.title}")
            .setContentText("загрузка")
            .setSmallIcon(R.drawable.downloading_icon)
        id = lastId
        lastId++
        if (!isChannelCreated) {
            createNotificationChannel()
        }
        isChannelCreated = true
        downLoadL(song, callback)
    }

    private fun downLoadL(
        song: Song,
        callback: (DownloadResult) -> Unit
    ): Int {
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
                val progressPercent = it.currentBytes * multiplier / it.totalBytes
                Log.d("Download", "progress: ${progressPercent.toInt()}")
                builder.setProgress(multiplier, progressPercent.toInt(), false)
                notificationManager.notify(
                    id,
                    builder.build()
                )
            }
            .start(
                object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        try {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                copyFileToDownloads(fileName)
                            }
                            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                                copyFileToDownloadsAPI29(fileName)
                            }
                            MetadataScanner(context, "$DOWNLOAD_DIR/$fileName", mediaStoreReader)
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

    private fun copyFileToDownloads(filename: String) {
        val src = File(context.filesDir, filename)
        val dst = File(DOWNLOAD_DIR, filename)
        try {
            src.copyTo(dst)
        } finally {
            src.delete()
        }
    }

    private fun File.copyTo(file: File) {
        inputStream().use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun copyFileToDownloadsAPI29(filename: String) {
        try {
            val downloadedFile = File(context.filesDir, filename)
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, downloadedFile.name)
                put(MediaStore.MediaColumns.MIME_TYPE, "mp3")
            }
            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            )
            context.contentResolver.openOutputStream(uri!!).use { outputStream ->
                val brr = ByteArray(1024)
                var len: Int
                val bufferedInputStream = BufferedInputStream(FileInputStream(File(context.filesDir, filename).absoluteFile))
                while ((bufferedInputStream.read(brr, 0, brr.size).also { len = it }) != -1) {
                    outputStream?.write(brr, 0, len)
                }
                outputStream?.flush()
                bufferedInputStream.close()
            }
        } finally {
            File(context.filesDir, filename).delete()
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "download"
            val descriptionText = "music downloading"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel("download", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }
}
