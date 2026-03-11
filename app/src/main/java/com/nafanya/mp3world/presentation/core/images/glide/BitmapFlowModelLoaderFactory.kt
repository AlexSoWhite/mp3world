package com.nafanya.mp3world.presentation.core.images.glide

import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.nafanya.mp3world.presentation.core.images.SongImageBitmapFactory
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import java.io.InputStream
import javax.inject.Inject

class BitmapFlowModelLoaderFactory @Inject constructor(
    private val songImageBitmapFactory: SongImageBitmapFactory,
) : ModelLoaderFactory<SongWrapper, InputStream> {

    override fun build(
        multiFactory: MultiModelLoaderFactory
    ): ModelLoader<SongWrapper, InputStream> {
        return BitmapFlowModelLoaderImpl(songImageBitmapFactory)
    }

    override fun teardown() {
        // nothing to do
    }
}
