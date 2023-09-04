package com.nafanya.mp3world.features.downloading.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.viewModel.ViewModelKey
import com.nafanya.mp3world.features.downloading.DownloadViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface DownloadModule {

    @Binds
    @[IntoMap ViewModelKey(DownloadViewModel::class)]
    fun providesDownloadViewModel(downloadViewModel: DownloadViewModel): ViewModel
}
