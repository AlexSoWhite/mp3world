package com.nafanya.mp3world.presentation.entrypoint.di

import com.nafanya.mp3world.presentation.entrypoint.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [EntrypointModule::class])
interface EntrypointComponent {

    fun inject(activity: MainActivity)
}

interface EntrypointComponentProvider {

    val entrypointComponent: EntrypointComponent
}
