package com.nafanya.mp3world.core.wrappers.di

import com.nafanya.mp3world.core.wrappers.ArtFactory
import com.nafanya.mp3world.core.wrappers.ArtFactoryImpl
import com.nafanya.mp3world.core.wrappers.SongImageBitmapFactory
import com.nafanya.mp3world.core.wrappers.SongImageBitmapFactoryImpl
import com.nafanya.mp3world.core.wrappers.UriFactory
import com.nafanya.mp3world.core.wrappers.UriFactoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface WrappersModule {

    @Binds
    fun artFactory(impl: ArtFactoryImpl): ArtFactory

    @Binds
    fun uriFactory(impl: UriFactoryImpl): UriFactory

    @Binds
    @Singleton
    fun songImageBitmapFactory(impl: SongImageBitmapFactoryImpl): SongImageBitmapFactory
}
