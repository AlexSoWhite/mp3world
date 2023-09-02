package com.nafanya.mp3world.core.wrappers.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.wrappers.SongWrapper
import java.io.InputStream

@GlideModule
class SongGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val factory = (context.applicationContext as PlayerApplication)
            .applicationComponent
            .bitmapFlowModelLoaderFactoryComponent
            .bitmapFlowModelLoaderFactory
        registry.prepend(
            SongWrapper::class.java,
            InputStream::class.java,
            factory
        )
    }
}
