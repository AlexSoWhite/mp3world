package com.nafanya.mp3world.core.utils.list_utils.title

import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for titling functionality. The easiest way to use it is to setup a [TitleProcessor].
 * Using this interface you can add dynamic title to the UI.
 */
interface TitleProcessorWrapper<T : List<*>> {

    val title: StateFlow<TitleModel>
}
