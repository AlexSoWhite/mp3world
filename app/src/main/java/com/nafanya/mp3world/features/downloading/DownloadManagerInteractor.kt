package com.nafanya.mp3world.features.downloading

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import androidx.annotation.WorkerThread
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Class that works with [android.app.DownloadManager] and handles result using [BroadcastReceiver]
 */
class DownloadManagerInteractor @Inject constructor(
    private val context: Context
) {

    @WorkerThread
    fun downloadFromUrl(url: String): Flow<String?> {
        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val fileName = createFileName(url)
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setTitle(createFileName(url))
        request.setNotificationVisibility(
            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
        )
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        val id = downloadManager.enqueue(request)
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        val receiver = DownloadReceiver(id, fileName)
        context.registerReceiver(receiver, filter)
        return receiver.nameFlow
    }

    inner class DownloadReceiver(
        private val id: Long,
        private val name: String
    ) : BroadcastReceiver() {

        private val mNameFlow = MutableStateFlow("")
        val nameFlow: StateFlow<String>
            get() = mNameFlow

        override fun onReceive(p0: Context?, intent: Intent?) {
            val receivedId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: -1
            if (id == receivedId) {
                mNameFlow.value = name
                context.unregisterReceiver(this)
            }
        }
    }

    private fun createFileName(url: String): String {
        var fileName = url
            .substring(url.lastIndexOf('/') + 1)
            .replace('_', ' ')
        fileName = fileName.substring(0, fileName.lastIndexOf(' ')) + ".mp3"
        return fileName
    }
}
