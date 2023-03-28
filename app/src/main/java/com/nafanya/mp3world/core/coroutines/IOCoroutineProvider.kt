package com.nafanya.mp3world.core.coroutines

import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

/**
 * Should be used to launch IO operations (interacting with disk and network)
 */
class IOCoroutineProvider @Inject constructor() {

    val ioScope = CoroutineScope(AppDispatchers.io)
}
