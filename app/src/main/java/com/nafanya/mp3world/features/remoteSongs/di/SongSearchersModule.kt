package com.nafanya.mp3world.features.remoteSongs.di

import com.nafanya.mp3world.features.remoteSongs.data.HITMO_TOP
import com.nafanya.mp3world.features.remoteSongs.data.HitmoTopSongSearcher
import com.nafanya.mp3world.features.remoteSongs.data.MUSMORE
import com.nafanya.mp3world.features.remoteSongs.data.MusMoreSongSearcher
import com.nafanya.mp3world.features.remoteSongs.data.SongSearcher
import com.nafanya.mp3world.features.remoteSongs.data.SongSearcherKey
import com.nafanya.mp3world.features.remoteSongs.data.ZVUCH
import com.nafanya.mp3world.features.remoteSongs.data.ZvuchSongSearcher
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SongSearchersModule {
    @Binds
    @[IntoMap SongSearcherKey(MUSMORE)]
    fun bindMusMore(songSearcher: MusMoreSongSearcher): SongSearcher

    @Binds
    @[IntoMap SongSearcherKey(HITMO_TOP)]
    fun bindHitmoTop(songSearcher: HitmoTopSongSearcher): SongSearcher

    @Binds
    @[IntoMap SongSearcherKey(ZVUCH)]
    fun bindZvuch(songSearcher: ZvuchSongSearcher): SongSearcher
}
