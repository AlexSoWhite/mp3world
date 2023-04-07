package com.nafanya.mp3world.core.stateMachines

sealed class LState {
    object Empty : State<Nothing>()
}

open class ListStateModel<T : List<*>> : StateModel<T>() {

    open fun empty(onStateChanged: (() -> Unit)? = null) {
        mCurrentState.value = LState.Empty
        onStateChanged?.invoke()
    }
}
