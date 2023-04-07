package com.nafanya.mp3world.core.stateMachines

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class State<out T> {
    class Initializing : State<Nothing>()
    class Loading : State<Nothing>()
    class Error(val error: kotlin.Error) : State<Nothing>()
    class Success<T>(val data: T) : State<T>()
    class Updated<T>(val data: T) : State<T>()
}

/**
 * @param <T> is a class for data source
 */
open class StateModel<T> {

    protected val mCurrentState = MutableStateFlow<State<T>>(State.Initializing())
    val currentState: StateFlow<State<T>> = mCurrentState.asStateFlow()

    /**
     * States from where [load] is allowed.
     */
    var allowedToLoad = setOf(State.Initializing::class)

    /**
     * States from where [success] is allowed.
     */
    var allowedToSuccess = setOf(State.Loading::class)

    /**
     * States from where [error] is allowed.
     */
    var allowedToError = setOf(State.Loading::class)

    /**
     * States from where [refresh] is not allowed.
     */
    var notAllowedToRefresh = setOf(State.Loading::class)

    /**
     * States from where [update] is not allowed
     */
    var notAllowedToUpdate = setOf(State.Loading::class)

    open fun load(onStateChanged: (() -> Unit)? = null) {
        if (currentState.value::class !in allowedToLoad) return
        mCurrentState.value = State.Loading()
        onStateChanged?.invoke()
    }

    open fun success(data: T, onStateChanged: (() -> Unit)? = null) {
        if (currentState.value::class in allowedToSuccess) {
            mCurrentState.value = State.Success(data)
            onStateChanged?.invoke()
        } else {
            update(data, onStateChanged)
        }
    }

    open fun error(error: Error, onStateChanged: (() -> Unit)? = null) {
        if (currentState.value::class !in allowedToError) return
        mCurrentState.value = State.Error(error)
        onStateChanged?.invoke()
    }

    open fun refresh(onStateChanged: (() -> Unit)? = null) {
        if (currentState.value::class in notAllowedToRefresh) return
        mCurrentState.value = State.Loading()
        onStateChanged?.invoke()
    }

    open fun update(data: T, onStateChanged: (() -> Unit)? = null) {
        if (currentState.value::class in notAllowedToUpdate) return
        mCurrentState.value = State.Updated(data)
        onStateChanged?.invoke()
    }
}
