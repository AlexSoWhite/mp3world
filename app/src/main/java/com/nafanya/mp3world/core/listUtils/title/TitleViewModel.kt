package com.nafanya.mp3world.core.listUtils.title

import androidx.lifecycle.LiveData

/**
 * Interface for titling functionality. Th easiest way to use it is to setup a [TitleProcessor].
 * Using this interface you can add dynamic title to the UI.
 */
interface TitleViewModel<T : List<*>> {

    val title: LiveData<String>
}
