package com.nafanya.mp3world.presentation.core.images.glide

import android.graphics.Bitmap
import android.util.Size
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.nafanya.mp3world.presentation.core.images.SongImageBitmapFactory
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class BitmapFlowModelDataFetcherImpl(
    private val model: SongWrapper,
    private val size: Size,
    private val songImageBitmapFactory: SongImageBitmapFactory,
) : DataFetcher<InputStream> {

    // todo: maybe Job, not SupervisorJob
    private val bitmapDataFetcherScope = CoroutineScope(SupervisorJob())
    private var jobs = mutableListOf<Job>()

    // todo: should check under what circumstances it is called because it definitely should not load image in application scope
    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        jobs.add(
            bitmapDataFetcherScope.launch {
                try {
                    // note: context is switched inside factory
                    val bitmap = songImageBitmapFactory.getBitmapForSong(model, size)
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
                    val data = stream.toByteArray()
                    callback.onDataReady(ByteArrayInputStream(data))
                } catch (e: IllegalStateException) {
                    // TODO error cathing
                }
            }
        )
    }

    override fun cleanup() {
        // nothing to do
    }

    // todo: maybe cancel the whole scope?
    override fun cancel() {
        jobs.forEach { it.cancel() }
        jobs.clear()
    }

    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.LOCAL
    }
}
