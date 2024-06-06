package com.nafanya.mp3world.core.utils.listUtils.recycler

@Suppress("UnnecessaryAbstractClass")
abstract class BaseListItem {
    abstract val itemType: Int
    protected abstract val data: Any
}
