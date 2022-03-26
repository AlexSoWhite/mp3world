package com.nafanya.mp3world.model.network

import android.os.Environment
import android.util.Log
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader

class DownloadService {

    fun startLoading(url: String) {
        var fileName = url
            .substring(url.lastIndexOf('/')+1)
            .replace('_', ' ')
        fileName = fileName.substring(0, fileName.lastIndexOf(' ')) + ".mp3"
        downLoad(url, fileName)
    }

    private fun downLoad(url: String, fileName: String): Int {
        val dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        return PRDownloader.download(url, dirPath, fileName)
            .build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {

                }

                override fun onError(error: Error?) {
                    Log.d("Download", error.toString())
                }

            })
    }
}
