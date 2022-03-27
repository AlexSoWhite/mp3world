package com.nafanya.mp3world.model.network

import android.os.Environment
import android.util.Log
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.nafanya.mp3world.model.wrappers.Song

class DownloadService {

    fun downLoad(song: Song, callback: () -> Unit): Int {
        val url = song.url!!
        var fileName = url
            .substring(url.lastIndexOf('/') + 1)
            .replace('_', ' ')
        fileName = fileName.substring(0, fileName.lastIndexOf(' ')) + ".mp3"
        val dirPath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        return PRDownloader.download(url, dirPath, fileName)
            .build()
            .start(
                object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        MetadataScanner.scan("$dirPath/$fileName")
                        callback()
                    }

                    override fun onError(error: Error?) {
                        Log.d("Download", error.toString())
                    }
                }
            )
    }
}
