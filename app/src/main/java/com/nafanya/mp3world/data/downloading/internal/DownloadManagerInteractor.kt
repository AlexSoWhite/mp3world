package com.nafanya.mp3world.data.downloading.internal

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import com.nafanya.mp3world.core.coroutines.DispatchersProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import androidx.core.net.toUri

/**
 * Class that works with [android.app.DownloadManager] and handles result using [BroadcastReceiver]
 *
 * todo: failed downloads are not handled probably
 */
internal class DownloadManagerInteractor(
    private val applicationContext: Context,
    private val dispatchersProvider: DispatchersProvider
) {

    /**
     * Downloads song from [url].
     */
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    suspend fun downloadFromUrl(url: String, fileName: String): String = withContext(dispatchersProvider.io) {
        val downloadManager = applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = url.toUri()
        val request = DownloadManager.Request(uri)
        request.setTitle(fileName)
        request.setNotificationVisibility(
            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
        )
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        val id = downloadManager.enqueue(request)
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        val receiver = DownloadReceiver(id, fileName)
        applicationContext.registerReceiver(receiver, filter)
        return@withContext receiver.nameFlow.filter { it.isNotBlank() }.first()
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
                applicationContext.unregisterReceiver(this)
            }
        }
    }
}
