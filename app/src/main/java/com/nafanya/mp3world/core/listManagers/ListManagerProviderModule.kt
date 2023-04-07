package com.nafanya.mp3world.core.listManagers

import dagger.Binds
import dagger.Module

@Module
interface ListManagerProviderModule {

    @Binds
    fun listManagerProvider(listManagerProvider: ListManagerProvider): ListManagerProvider
}
