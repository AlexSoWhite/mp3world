package com.nafanya.mp3world.core.wrappers.glide

import android.graphics.Bitmap
import android.util.Size
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.wrappers.SongImageBitmapFactory
import com.nafanya.mp3world.core.wrappers.SongWrapper
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BitmapFlowModelDataFetcherImpl(
    private val model: SongWrapper,
    private val size: Size,
    private val songImageBitmapFactory: SongImageBitmapFactory,
    private val ioCoroutineProvider: IOCoroutineProvider
) : DataFetcher<InputStream> {

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        ioCoroutineProvider.ioScope.launch {
            try {
                songImageBitmapFactory.getBitmapForSong(model, size).collectLatest {
                    val stream = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.PNG, 0, stream)
                    val data = stream.toByteArray()
                    callback.onDataReady(ByteArrayInputStream(data))
                }
            } catch (e: IllegalStateException) {
                // TODO error cathing
            }
        }
    }

    override fun cleanup() {
        // nothing to do
    }

    override fun cancel() {
        // nothing to do
    }

    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.LOCAL
    }
}
