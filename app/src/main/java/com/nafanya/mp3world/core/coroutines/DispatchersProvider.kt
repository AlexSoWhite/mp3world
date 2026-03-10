package com.nafanya.mp3world.core.coroutines

import kotlinx.coroutines.Dispatchers

class DispatchersProvider {
    val io = Dispatchers.IO
    val main = Dispatchers.Main
    val default = Dispatchers.Default
    val unconfined = Dispatchers.Unconfined
}
