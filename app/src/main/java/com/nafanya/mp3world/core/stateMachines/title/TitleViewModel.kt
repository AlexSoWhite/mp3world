package com.nafanya.mp3world.core.stateMachines.title

import com.nafanya.mp3world.core.stateMachines.LState
import com.nafanya.mp3world.core.stateMachines.State
import com.nafanya.mp3world.core.stateMachines.StateModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Interface to trigger title. To use it's functionality, invoke [startListeningModelForTitle].
 */
interface TitleViewModel<T : List<*>> {

    val baseTitle: String
    val mTitle: MutableStateFlow<String>
    val title: StateFlow<String>
        get() = mTitle.asStateFlow()

    val stateMapper: (suspend (State<T>) -> State<T>)?

    suspend fun StateModel<T>.startListeningModelForTitle() {
        this.currentState.collect { rawState ->
            val state = stateMapper?.invoke(rawState) ?: rawState
            mTitle.value = when (state) {
                is State.Loading -> renderLoading()
                is State.Error -> renderError(state.error)
                is State.Success -> renderSuccess(state.data)
                is State.Initializing -> renderInitializing()
                is State.Updated -> renderUpdated(state.data)
                is LState.Empty -> renderEmpty()
            }
        }
    }

    fun renderLoading() = baseTitle

    fun renderError(error: Error) = baseTitle

    fun renderSuccess(data: T) = "$baseTitle (${data.size})"

    fun renderInitializing() = baseTitle

    fun renderUpdated(data: T) = "$baseTitle (${data.size})"

    fun renderEmpty() = baseTitle
}
