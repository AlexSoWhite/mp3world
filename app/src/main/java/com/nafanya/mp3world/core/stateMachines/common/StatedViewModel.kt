package com.nafanya.mp3world.core.stateMachines.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.stateMachines.State
import com.nafanya.mp3world.core.stateMachines.StateModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class Data<T> {
    class Success<T>(val data: T) : Data<T>()
    class Error<T>(val error: kotlin.Error) : Data<T>()
}

/**
 * Class that manages state of [model]. To use it's functionality, invoke [setDataSource].
 */
abstract class StatedViewModel<T>(
    protected val model: StateModel<T> = StateModel()
) : ViewModel() {

    val state: StateFlow<State<T>> = model.currentState

    open fun setDataSource(dataSource: Flow<Data<T>>) {
        viewModelScope.launch {
            dataSource.collect {
                processData(it)
            }
        }
    }

    protected open fun processData(data: Data<T>) {
        when (data) {
            is Data.Error -> onError(data.error)
            is Data.Success -> onSuccess(data.data)
        }
    }

    protected open fun onError(error: Error) = model.error(error)

    protected open fun onSuccess(data: T) = model.success(data)

    open fun refresh(callback: () -> Unit) = model.refresh { callback() }
}
