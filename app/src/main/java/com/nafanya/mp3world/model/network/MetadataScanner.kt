package com.nafanya.mp3world.model.network

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.dependencies.PlayerApplication
import com.nafanya.mp3world.model.listManagers.MediaStoreReader
import com.nafanya.mp3world.viewmodel.DownloadViewModel
import javax.inject.Inject

/**
 * Class that adds song to MediaStore with all its metadata.
 */
class MetadataScanner(
    var context: Context,
    var path: String,
    var mediaStoreReader: MediaStoreReader
) {

    inner class ScannerClient : MediaScannerConnection.MediaScannerConnectionClient {
        override fun onScanCompleted(p0: String?, p1: Uri?) {
            p1?.toString()?.let {
                mediaStoreReader.reset()
                Log.d("Scan", it)
            }
        }

        override fun onMediaScannerConnected() {
            scan(path)
        }
    }

    private var scanner: MediaScannerConnection = MediaScannerConnection(
        context,
        ScannerClient()
    )

    init {
        scanner.connect()
    }

    private fun scan(path: String) {
        scanner.scanFile(path, "mp3")
    }
}
