package com.nafanya.mp3world.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

inline fun <reified A, reified R> ((A) -> R).inScope(
    scope: CoroutineScope,
    args: A
) {
    scope.launch {
        this@inScope.invoke(args)
    }
}

inline fun <reified T> Flow<T>.collectInScope(
    scope: CoroutineScope,
    crossinline onCollect: (T) -> Unit
) {
    scope.launch {
        this@collectInScope.collect {
            onCollect.invoke(it)
        }
    }
}

inline fun <reified T> Flow<T>.collectLatestInScope(
    scope: CoroutineScope,
    crossinline onCollect: (T) -> Unit
) {
    scope.launch {
        this@collectLatestInScope.collectLatest {
            onCollect.invoke(it)
        }
    }
}
