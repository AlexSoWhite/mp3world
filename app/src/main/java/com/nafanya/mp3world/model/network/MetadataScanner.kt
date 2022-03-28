package com.nafanya.mp3world.model.network

import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log
import com.nafanya.mp3world.model.dependencies.PlayerApplication
import com.nafanya.mp3world.model.listManagers.MediaStoreReader

class MetadataScanner {

    private var context = PlayerApplication.context()
    private var scanner: MediaScannerConnection = MediaScannerConnection(
        context,
        object : MediaScannerConnection.MediaScannerConnectionClient {
            override fun onScanCompleted(p0: String?, p1: Uri?) {
                p1?.toString()?.let {
                    MediaStoreReader().reset()
                    Log.d("Scan", it)
                }
            }

            override fun onMediaScannerConnected() {
                Log.d("Scan", "connected")
            }
        }
    )

    init {
        scanner.connect()
    }

    fun scan(path: String) {
        scanner.scanFile(path, "mp3")
    }
}
