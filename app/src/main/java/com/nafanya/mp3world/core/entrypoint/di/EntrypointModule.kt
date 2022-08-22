package com.nafanya.mp3world.core.entrypoint.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.ViewModelKey
import com.nafanya.mp3world.core.entrypoint.InitialViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface EntrypointModule {

    @Binds
    @[IntoMap ViewModelKey(InitialViewModel::class)]
    fun initialViewModel(initialViewModel: InitialViewModel): ViewModel
}
