package com.nafanya.mp3world.features.remoteSongs.di

import com.nafanya.mp3world.features.remoteSongs.songSearchers.HITMO_TOP
import com.nafanya.mp3world.features.remoteSongs.songSearchers.HitmoTopSongSearcher
import com.nafanya.mp3world.features.remoteSongs.songSearchers.MUSMORE
import com.nafanya.mp3world.features.remoteSongs.songSearchers.MusMoreSongSearcher
import com.nafanya.mp3world.features.remoteSongs.songSearchers.SongSearcher
import com.nafanya.mp3world.features.remoteSongs.songSearchers.SongSearcherKey
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
}
