package com.nafanya.mp3world.model.network

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log

@Suppress("StaticFieldLeak")
object MetadataScanner {

    private lateinit var context: Context
    private lateinit var scanner: MediaScannerConnection

    fun context(context: Context) {
        this.context = context
        scanner = MediaScannerConnection(
            MetadataScanner.context,
            object : MediaScannerConnection.MediaScannerConnectionClient {
                override fun onScanCompleted(p0: String?, p1: Uri?) {
                    p1?.toString()?.let { Log.d("Scan", it) }
                }

                override fun onMediaScannerConnected() {
                    Log.d("Scan", "connected")
                }
            }
        )
        scanner.connect()
    }

    fun scan(path: String) {
        scanner.scanFile(path, "mp3")
    }
}
