package com.nafanya.mp3world.core.coroutines

import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

class IOCoroutineProvider @Inject constructor() {

    val ioScope = CoroutineScope(AppDispatchers.io)
}
