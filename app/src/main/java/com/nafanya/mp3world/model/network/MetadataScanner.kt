package com.nafanya.mp3world.model.network

import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log
import com.nafanya.mp3world.model.dependencies.PlayerApplication
import com.nafanya.mp3world.model.listManagers.MediaStoreReader

/**
 * Class that adds song to MediaStore with all its metadata.
 */
class MetadataScanner(
    private val path: String
) {

    inner class ScannerClient : MediaScannerConnection.MediaScannerConnectionClient {
        override fun onScanCompleted(p0: String?, p1: Uri?) {
            p1?.toString()?.let {
                MediaStoreReader().reset()
                Log.d("Scan", it)
            }
        }

        override fun onMediaScannerConnected() {
            scan()
        }
    }

    private var context = PlayerApplication.context()
    private var scanner: MediaScannerConnection = MediaScannerConnection(
        context,
        ScannerClient()
    )

    init {
        scanner.connect()
    }

    fun scan() {
        scanner.scanFile(path, "mp3")
    }
}
