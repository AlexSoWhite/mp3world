package com.nafanya.mp3world.core.wrappers.glide

import android.util.Size
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.signature.ObjectKey
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.wrappers.SongImageBitmapFactory
import com.nafanya.mp3world.core.wrappers.SongWrapper
import java.io.InputStream

class BitmapFlowModelLoaderImpl(
    private val songImageBitmapFactory: SongImageBitmapFactory,
    private val ioCoroutineProvider: IOCoroutineProvider
) : ModelLoader<SongWrapper, InputStream> {

    override fun buildLoadData(
        model: SongWrapper,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream> {
        return ModelLoader.LoadData(
            ObjectKey(model),
            BitmapFlowModelDataFetcherImpl(
                model,
                Size(width, height),
                songImageBitmapFactory,
                ioCoroutineProvider
            )
        )
    }

    override fun handles(model: SongWrapper): Boolean {
        return true
    }
}
