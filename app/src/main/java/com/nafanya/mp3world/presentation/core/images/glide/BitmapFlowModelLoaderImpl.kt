package com.nafanya.mp3world.presentation.core.images.glide

import android.util.Size
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.signature.ObjectKey
import com.nafanya.mp3world.presentation.core.images.SongImageBitmapFactory
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import java.io.InputStream

class BitmapFlowModelLoaderImpl(
    private val songImageBitmapFactory: SongImageBitmapFactory
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
                songImageBitmapFactory
            )
        )
    }

    override fun handles(model: SongWrapper): Boolean {
        return true
    }
}
