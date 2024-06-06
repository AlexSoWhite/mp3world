package com.nafanya.mp3world.core.stateMachines

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class State<out T> {
    object Initializing : State<Nothing>()
    object Loading : State<Nothing>()
    class Error(val error: kotlin.Error) : State<Nothing>()
    class Success<T>(val data: T) : State<T>()
    class Updated<T>(val data: T) : State<T>()
}

/**
 * Class that can change its state and hold and notify its subscribers about it.
 * States of the machine are designed to be observed by UI.
 * @param <T> is a class for data source
 */
open class StateModel<T> {

    protected val mCurrentState = MutableStateFlow<State<T>>(State.Initializing)
    val currentState: StateFlow<State<T>> = mCurrentState.asStateFlow()

    /**
     * States from where [load] is allowed.
     */
    private val allowedToLoad = setOf(State.Initializing::class)

    /**
     * States from where [success] is allowed.
     */
    private val allowedToSuccess = setOf(State.Loading::class)

    /**
     * States from where [error] is allowed.
     */
    private val allowedToError = setOf(State.Loading::class)

    /**
     * States from where [refresh] is not allowed.
     */
    private val notAllowedToRefresh = setOf(State.Loading::class)

    /**
     * States from where [update] is not allowed
     */
    private val notAllowedToUpdate = setOf(State.Loading::class)

    /**
     * If [currentState] is in [allowedToLoad], puts [StateModel] in [State.Loading] and after
     * [currentState] changed to [State.Loading] invokes the [onStateChanged].
     */
    open fun load(onStateChanged: (() -> Unit)? = null) {
        if (currentState.value::class !in allowedToLoad) return
        mCurrentState.value = State.Loading
        onStateChanged?.invoke()
    }

    /**
     * If [currentState] is in [allowedToSuccess], puts [StateModel] in [State.Success] and invokes
     * the [onStateChanged] after [currentState] changed to [State.Success], otherwise calls the
     * [update] method
     */
    open fun success(data: T, onStateChanged: (() -> Unit)? = null) {
        if (currentState.value::class in allowedToSuccess) {
            mCurrentState.value = State.Success(data)
            onStateChanged?.invoke()
        } else {
            update(data, onStateChanged)
        }
    }

    /**
     * If [currentState] is in [allowedToError], puts [StateModel] in [State.Error] and invokes
     * [onStateChanged] after [currentState] changed to [State.Error]
     */
    open fun error(error: Error, onStateChanged: (() -> Unit)? = null) {
        if (currentState.value::class !in allowedToError) return
        mCurrentState.value = State.Error(error)
        onStateChanged?.invoke()
    }

    /**
     * If [currentState] is not in [notAllowedToRefresh], puts [StateModel] in [State.Loading] and
     * invokes [onStateChanged] after [currentState] changed to [State.Loading]
     */
    open fun refresh(onStateChanged: (() -> Unit)? = null) {
        if (currentState.value::class in notAllowedToRefresh) return
        mCurrentState.value = State.Loading
        onStateChanged?.invoke()
    }

    /**
     * If [currentState] is not in [notAllowedToUpdate], puts [StateModel] in [State.Updated] and
     * invokes [onStateChanged] after [currentState] changed to [State.Updated]. Designed t separate
     * scenarios with manual updates and background/automatic updates
     */
    open fun update(data: T, onStateChanged: (() -> Unit)? = null) {
        if (currentState.value::class in notAllowedToUpdate) return
        mCurrentState.value = State.Updated(data)
        onStateChanged?.invoke()
    }
}
