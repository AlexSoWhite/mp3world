package com.nafanya.mp3world.core.coroutines

import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

/**
 * Should be used for UI operations
 */
class MainCoroutineProvider @Inject constructor() {

    val mainScope = CoroutineScope(AppDispatchers.main)
}
