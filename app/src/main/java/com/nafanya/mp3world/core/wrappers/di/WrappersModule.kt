package com.nafanya.mp3world.core.wrappers.di

import com.nafanya.mp3world.core.wrappers.images.SongImageBitmapFactory
import com.nafanya.mp3world.core.wrappers.images.SongImageBitmapFactoryImpl
import com.nafanya.mp3world.core.wrappers.song.UriFactory
import com.nafanya.mp3world.core.wrappers.song.UriFactoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface WrappersModule {

    @Binds
    fun uriFactory(impl: UriFactoryImpl): UriFactory

    @Binds
    @Singleton
    fun songImageBitmapFactory(impl: SongImageBitmapFactoryImpl): SongImageBitmapFactory
}
