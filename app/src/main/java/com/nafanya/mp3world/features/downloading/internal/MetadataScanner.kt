package com.nafanya.mp3world.features.downloading.internal

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log

enum class ScannerResult {
    SUCCESS,
    FAILED
}

/**
 * Class that adds song to MediaStore with all its metadata.
 */
internal class MetadataScanner(
    context: Context,
    private val path: String,
    private val onCompleteCallback: (ScannerResult) -> Unit
) {

    private val scanner: MediaScannerConnection by lazy {
        MediaScannerConnection(
            context,
            ScannerClient(onCompleteCallback)
        )
    }

    init {
        scanner.connect()
    }

    inner class ScannerClient(
        private val onCompleteCallback: (ScannerResult) -> Unit
    ) : MediaScannerConnection.MediaScannerConnectionClient {
        override fun onScanCompleted(p0: String?, p1: Uri?) {
            if (p1 != null) {
                Log.d("Scan", p1.toString())
                onCompleteCallback(ScannerResult.SUCCESS)
            } else {
                onCompleteCallback(ScannerResult.FAILED)
            }
        }
        override fun onMediaScannerConnected() {
            scan(path)
        }
    }

    private fun scan(path: String) {
        scanner.scanFile(path, "mp3")
    }
}
