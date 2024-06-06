package com.nafanya.mp3world.core.wrappers.di

import com.nafanya.mp3world.core.wrappers.images.glide.BitmapFlowModelLoaderFactory
import dagger.Subcomponent

@Subcomponent
interface BitmapFlowModelLoaderFactoryComponent {

    val bitmapFlowModelLoaderFactory: BitmapFlowModelLoaderFactory
}

interface BitmapFlowModelLoaderFactoryComponentProvider {

    val bitmapFlowModelLoaderFactoryComponent: BitmapFlowModelLoaderFactoryComponent
}
