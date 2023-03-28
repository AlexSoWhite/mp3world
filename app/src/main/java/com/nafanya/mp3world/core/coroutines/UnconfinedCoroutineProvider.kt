package com.nafanya.mp3world.core.coroutines

import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

class UnconfinedCoroutineProvider @Inject constructor() {

    val unconfinedScope = CoroutineScope(AppDispatchers.unconfined)
}
