package com.nafanya.mp3world.features.remote_songs.di

import com.nafanya.mp3world.data.remote_songs.HITMO_TOP
import com.nafanya.mp3world.data.remote_songs.HitmoTopSongSearcher
import com.nafanya.mp3world.data.remote_songs.MUSMORE
import com.nafanya.mp3world.data.remote_songs.MusMoreSongSearcher
import com.nafanya.mp3world.data.remote_songs.SongSearcher
import com.nafanya.mp3world.data.remote_songs.SongSearcherKey
import com.nafanya.mp3world.data.remote_songs.ZVUCH
import com.nafanya.mp3world.data.remote_songs.ZvuchSongSearcher
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
