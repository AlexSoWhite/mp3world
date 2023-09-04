package com.nafanya.mp3world.core.di.viewModel

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
interface ViewModelFactoryModule {

    @Binds
    fun viewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
