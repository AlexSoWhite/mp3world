package com.nafanya.mp3world.core.viewModel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        providers[modelClass]?.let {
            return it.get() as T
        }
        throw IllegalArgumentException("No such provider for " + modelClass.canonicalName)
    }
}

inline fun <reified VM : ViewModel> ViewModelProvider.Factory.getViewModel(
    activity: FragmentActivity
) = ViewModelProvider(activity, this)[VM::class.java]

inline fun <reified VM : ViewModel> ViewModelProvider.Factory.getViewModel(
    fragment: Fragment
) = ViewModelProvider(fragment, this)[VM::class.java]
