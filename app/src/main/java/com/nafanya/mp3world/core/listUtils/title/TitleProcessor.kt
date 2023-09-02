package com.nafanya.mp3world.core.listUtils.title

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.stateMachines.LState
import com.nafanya.mp3world.core.stateMachines.State
import com.nafanya.mp3world.core.stateMachines.StateModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Class used to add titling functionality to [StateModel]
 */
class TitleProcessor<T : List<*>> {

    private var baseTitle: String = ""

    private val mTitle = MutableLiveData(baseTitle)

    private lateinit var mScope: CoroutineScope

    private lateinit var mModel: StateModel<T>

    val title: LiveData<String>
        get() = mTitle

    /**
     * Subscribes to the current state of the [model] in [scope] to render suitable title for
     * list content
     */
    fun setup(model: StateModel<T>, scope: CoroutineScope) {
        mModel = model
        mScope = scope
        mScope.launch {
            mModel.currentState.collectLatest { state ->
                proceedState(state)
            }
        }
    }

    /**
     * Setting base title, must be called after [setup]
     */
    fun setBaseTitle(title: String) {
        baseTitle = title
        proceedState(mModel.currentState.value)
    }

    private fun proceedState(state: State<T>) {
        mTitle.value = convertStateToTitle(state, baseTitle)
    }
}

fun convertStateToTitle(state: State<List<*>>, baseTitle: String): String {
    return when (state) {
        is State.Loading -> baseTitle
        is State.Error -> baseTitle
        is State.Success -> "$baseTitle (${state.data.size})"
        is State.Initializing -> baseTitle
        is State.Updated -> "$baseTitle (${state.data.size})"
        is LState.Empty -> baseTitle
    }
}
