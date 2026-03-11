package com.nafanya.mp3world.core.utils.list_utils.title

import android.content.Context
import androidx.annotation.StringRes
import com.nafanya.mp3world.core.state_machines.LState
import com.nafanya.mp3world.core.state_machines.State
import com.nafanya.mp3world.core.state_machines.StateModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TitleModel private constructor(
    private var baseString: String? = null,
    @StringRes private var baseResource: Int? = null,
    private var count: Int? = null
) {

    internal class Builder {
        private var baseString: String? = null
        @StringRes private var baseResource: Int? = null
        private var count: Int? = null

        fun withBaseString(string: String): Builder {
            this.baseString = string
            return this
        }

        fun withBaseResource(@StringRes res: Int): Builder {
            this.baseResource = res
            return this
        }

        fun withCount(count: Int): Builder {
            this.count = count
            return this
        }

        fun build(): TitleModel {
            return TitleModel(baseString, baseResource, count)
        }
    }

    fun getText(context: Context): String {
        val base = if (baseString != null) {
            baseString!!
        } else {
            context.getString(baseResource!!)
        }
        return if (count == null) {
            base
        } else {
            "$base ($count)"
        }
    }
}

/**
 * Class used to add titling functionality to [StateModel]
 */
class TitleProcessor<T : List<*>>(
    private val baseTitleString: String? = null,
    @StringRes private val baseTitleRes: Int? = null
) {

    private lateinit var scope: CoroutineScope
    private lateinit var model: StateModel<T>

    private val _title = MutableStateFlow(
        TitleModel.Builder().setBase().build()
    )
    val title: StateFlow<TitleModel> get() = _title.asStateFlow()

    private fun TitleModel.Builder.setBase(): TitleModel.Builder {
        return apply {
            if (baseTitleString != null) {
                withBaseString(baseTitleString)
            } else if (baseTitleRes != null) {
                withBaseResource(baseTitleRes)
            }
        }
    }

    /**
     * Subscribes to the current state of the [model] in [scope] to render suitable title for
     * list content
     */
    fun setup(model: StateModel<T>, scope: CoroutineScope) {
        this.model = model
        this.scope = scope
        this.scope.launch {
            this@TitleProcessor.model.currentState.collectLatest { state ->
                processState(state)
            }
        }
    }

    private fun processState(state: State<T>) {
        val builder = TitleModel.Builder().setBase()
        val count = convertStateToCount(state)
        if (count != null) {
            builder.withCount(count)
        }
        _title.value = builder.build()
    }
}

fun convertStateToCount(state: State<List<*>>): Int? {
    return when (state) {
        LState.Empty, is State.Error, State.Initializing, State.Loading -> null
        is State.Success -> state.data.size
        is State.Updated -> state.data.size
    }
}
