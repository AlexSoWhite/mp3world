package com.nafanya.mp3world.core.coroutines

import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

/**
 * Should be used for computations and general background work
 */
class DefaultCoroutineProvider @Inject constructor() {

    val defaultScope = CoroutineScope(AppDispatchers.default)
}
