package com.nafanya.mp3world.core.coroutines

import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

class MainCoroutineProvider @Inject constructor() {

    val mainScope = CoroutineScope(AppDispatchers.main)
}
