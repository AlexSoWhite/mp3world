package com.nafanya.mp3world.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun <reified A, reified R> ((A) -> R).inScope(
    scope: CoroutineScope,
    args: A
) {
    scope.launch {
        this@inScope.invoke(args)
    }
}
