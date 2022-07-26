package com.nafanya.mp3world.core.di

import android.content.Context
import com.nafanya.player.PlayerInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlayerModule {

    @Provides
    @Singleton
    fun playerInteractor(context: Context) = PlayerInteractor(context)
}
