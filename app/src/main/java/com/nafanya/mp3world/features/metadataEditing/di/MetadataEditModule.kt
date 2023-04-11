package com.nafanya.mp3world.features.metadataEditing.di

import dagger.Module
import dagger.Provides
import org.cmc.music.myid3.MyID3

@Module
class MetadataEditModule {

    @Provides
    fun provideID3(): MyID3 {
        return MyID3()
    }
}
