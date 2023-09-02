package com.nafanya.mp3world.core.di

import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface ViewModelFactoryModule {

    @Binds
    fun viewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
