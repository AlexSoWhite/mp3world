package com.nafanya.mp3world.features.metadataEditing.di

import com.nafanya.mp3world.features.metadataEditing.MetadataEditFragment
import dagger.Subcomponent

@Subcomponent()
interface MetadataEditComponent {

    fun inject(metadataEditFragment: MetadataEditFragment)
}

interface MetadataEditComponentProvider {

    val metadataEditComponent: MetadataEditComponent
}
