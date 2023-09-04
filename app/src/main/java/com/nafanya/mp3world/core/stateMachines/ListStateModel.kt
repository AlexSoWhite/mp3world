package com.nafanya.mp3world.core.stateMachines

sealed class LState {
    object Empty : State<Nothing>()
}

/**
 * Class that extends [StateModel] adding [LState.Empty] to machine
 */
open class ListStateModel<T : List<*>> : StateModel<T>() {

    private val notAllowedToEmpty = setOf(State.Initializing::class)

    /**
     * If [currentState] is not in [notAllowedToEmpty], puts [ListStateModel] in [LState.Empty] and
     * invokes [onStateChanged] after [currentState] changed to [LState.Empty]
     */
    open fun empty(onStateChanged: (() -> Unit)? = null) {
        if (currentState.value::class in notAllowedToEmpty) return
        mCurrentState.value = LState.Empty
        onStateChanged?.invoke()
    }
}
