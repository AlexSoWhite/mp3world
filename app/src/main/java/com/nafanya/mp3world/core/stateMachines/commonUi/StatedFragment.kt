package com.nafanya.mp3world.core.stateMachines.commonUi

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.stateMachines.State
import com.nafanya.mp3world.core.commonUi.BaseFragment
import com.nafanya.mp3world.core.coroutines.collectInScope
import dagger.Lazy
import javax.inject.Inject

abstract class StatedFragment<VB : ViewBinding, T> : BaseFragment<VB>() {

    @Inject
    lateinit var factory: Lazy<ViewModelProvider.Factory>

    abstract val statedViewModel: StatedViewModel<T>

    abstract fun onInject(applicationComponent: ApplicationComponent)

    override fun onAttach(context: Context) {
        onInject((requireActivity().application as PlayerApplication).applicationComponent)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statedViewModel.state.collectInScope(lifecycleScope) {
            renderState(it)
        }
    }

    protected open fun renderState(state: State<T>) = when (state) {
        is State.Error -> renderError(state.error)
        is State.Initializing -> renderInitializing()
        is State.Loading -> renderLoading()
        is State.Success -> renderSuccess(state.data)
        is State.Updated -> renderUpdated(state.data)
        else -> throw IllegalStateException("Unknown state ${state.javaClass.canonicalName}")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressed()
            return true
        }
        return false
    }

    abstract fun renderError(error: Error)

    abstract fun renderInitializing()

    abstract fun renderLoading()

    abstract fun renderSuccess(data: T)

    abstract fun renderUpdated(data: T)
}
