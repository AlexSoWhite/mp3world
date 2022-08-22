package com.nafanya.mp3world.core.listUtils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.nafanya.mp3world.core.listUtils.recycler.BaseListItem
import java.lang.IllegalStateException

enum class ListState {
    IS_LOADING,
    IS_LOADED,
    IS_EMPTY,
    IS_UPDATED
}

/**
 * Base view model to render recycler states.
 * @param dataSource Source from where the data to display will be taken.
 * List considered as empty if the value from this source is empty.
 */
@Suppress("TooManyFunctions")
abstract class StateMachine<DataUnit, LI : BaseListItem>(
    var dataSource: LiveData<List<DataUnit>?>
) : ViewModel() {

    private val mListState = MutableLiveData(ListState.IS_LOADING)
    val listState: LiveData<ListState>
        get() = mListState

    private val mTitle: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String>
        get() = mTitle

    /**
     * Method to convert raw data to actual items to display.
     * StateMachine expects that this method to return all the items which should be displayed.
     */
    protected abstract fun List<DataUnit>.asListItems(): List<LI>

    /**
     * Actual data that to display, listens [dataSource].
     * Items which should be displayed are gathered from this LiveData using [asListItems] and, by default,
     * [containerSize] gets it's value based on this LiveData.
     */
    val data: MutableLiveData<List<DataUnit>?> = MutableLiveData()

    /**
     * Contains list of items which should be displayed in the recycler view.
     */
    val listItems: LiveData<List<LI>>
        get() = data.map { it?.asListItems() ?: listOf() }

    private var dataSourceObserver = Observer<List<DataUnit>?> { data.value = it }

    private var containerSize = MutableLiveData<Int>()
    private val containerSizeObserver = Observer<Int> { containerSize.value = it }
    private var containerSizeSource = data.map { it?.size ?: 0 }

    private val titleBase = MutableLiveData("")
    val compositeObservable = CompositeObservable()

    init {
        mTitle.value = titleBase.value
        with(compositeObservable) {
            addObserver(listState) {
                mTitle.value = when (it) {
                    ListState.IS_LOADING -> renderListLoadingTitle()
                    ListState.IS_LOADED -> renderListLoadedTitle()
                    ListState.IS_EMPTY -> renderListEmptyTitle()
                    ListState.IS_UPDATED -> renderListUpdatedTitle()
                    else -> throw UnknownStateException(it)
                }
            }
            addObserver(titleBase) {
                mTitle.value = when (listState.value) {
                    ListState.IS_LOADING -> renderListLoadingTitle()
                    ListState.IS_LOADED -> renderListLoadedTitle()
                    ListState.IS_EMPTY -> renderListEmptyTitle()
                    ListState.IS_UPDATED -> renderListUpdatedTitle()
                    else -> throw UnknownStateException(listState.value)
                }
            }
            addObserver(dataSource, dataSourceObserver)
            addObserver(containerSizeSource, containerSizeObserver)
            addStateUpdateTrigger(data)
            addStateUpdateTrigger(containerSize)
        }
    }

    protected fun resetContainerSizeSource(
        newSource: LiveData<Int>
    ) {
        compositeObservable.removeObserver(containerSizeSource, containerSizeObserver)
        containerSizeSource = newSource
        compositeObservable.addObserver(containerSizeSource, containerSizeObserver)
    }

    fun resetDataSourceObserver(
        newObserver: Observer<List<DataUnit>?>
    ) {
        compositeObservable.removeObserver(dataSource, dataSourceObserver)
        dataSourceObserver = newObserver
        compositeObservable.addObserver(dataSource, dataSourceObserver)
    }

    protected fun resetDataSource(
        newSource: LiveData<List<DataUnit>?>
    ) {
        compositeObservable.removeObserver(dataSource, dataSourceObserver)
        dataSource = newSource
        compositeObservable.addObserver(dataSource, dataSourceObserver)
    }

    protected fun resetInitialTitle(
        title: String
    ) {
        titleBase.value = title
    }

    /**
     * Changes states based on current state and data.
     */
    fun updateState() {
        mListState.value = when (listState.value) {
            ListState.IS_LOADING -> updateFromLoadingState()
            ListState.IS_LOADED -> updateFromLoadedState()
            ListState.IS_EMPTY -> updateFromEmptyState()
            ListState.IS_UPDATED -> updateFromUpdatedState()
            else -> throw UnknownStateException(listState.value)
        }
    }

    /**
     * @param trigger LiveData, which, when changed, will trigger [updateState] if [UpdateTriggerPolicy.shouldTrigger]
     * returns true.
     * @param updateTriggerPolicy instance of [UpdateTriggerPolicy]
     */
    inline fun <reified T : Any> addStateUpdateTrigger(
        trigger: LiveData<T?>,
        updateTriggerPolicy: UpdateTriggerPolicy = UpdateTriggerPolicy()
    ) {
        compositeObservable.addObserver(trigger) {
            if (updateTriggerPolicy.shouldTrigger(it)) {
                updateState()
            }
        }
    }

    /**
     * @param trigger LiveData, which, when changed, will update [data] from [dataSource]
     * if [UpdateTriggerPolicy.shouldTrigger] returns true.
     * @param updateTriggerPolicy instance of [UpdateTriggerPolicy]
     */
    inline fun <reified T : Any> addDataUpdateTrigger(
        trigger: LiveData<T?>,
        updateTriggerPolicy: UpdateTriggerPolicy = UpdateTriggerPolicy()
    ) {
        compositeObservable.addObserver(trigger) {
            if (updateTriggerPolicy.shouldTrigger(it)) {
                data.value = dataSource.value
            }
        }
    }

    protected open fun updateFromLoadingState(): ListState {
        return when {
            isEmpty() -> ListState.IS_EMPTY
            else -> ListState.IS_LOADED
        }
    }

    private fun navigateFromStaticState(): ListState {
        return when {
            isEmpty() -> ListState.IS_EMPTY
            else -> ListState.IS_UPDATED
        }
    }

    protected open fun updateFromLoadedState(): ListState {
        return navigateFromStaticState()
    }

    protected open fun updateFromEmptyState(): ListState {
        return when {
            isEmpty() -> ListState.IS_EMPTY
            else -> ListState.IS_LOADED
        }
    }

    protected open fun updateFromUpdatedState(): ListState {
        return navigateFromStaticState()
    }

    /**
     * This method is used to count size of displayed list.
     * Counted value is used in screen title.
     */
    private fun getContainerSize(): Int {
        return containerSize.value ?: 0
    }

    private fun isEmpty(): Boolean {
        return data.value.isNullOrEmpty()
    }

    /**
     * Method calls automatically when States changes to States.IS_LOADING
     */
    protected open fun renderListLoadingTitle() = titleBase.value

    /**
     * Method calls automatically when States changes to States.IS_LOADED
     */
    protected open fun renderListLoadedTitle() = titleBase.value + " (${getContainerSize()})"

    /**
     * Method calls automatically when States changes to States.IS_EMPTY
     */
    protected open fun renderListEmptyTitle() = titleBase.value

    /**
     * Method calls automatically when States changes to States.IS_UPDATED
     */
    protected open fun renderListUpdatedTitle() = titleBase.value + " (${getContainerSize()})"

    override fun onCleared() {
        super.onCleared()
        compositeObservable.removeAll()
    }
}

class UnknownStateException(state: ListState?) : IllegalStateException("Unknown state $state")

class UpdateTriggerPolicy(
    private val onCheck: (Any?) -> Boolean = { true }
) {
    fun shouldTrigger(value: Any?): Boolean = onCheck(value)
}

class CompositeObservable {

    val observersMap = mutableMapOf<LiveData<out Any?>, Observer<in Any?>>()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any?> addObserver(
        source: LiveData<T>,
        observer: Observer<T>
    ) {
        source.observeForever(observer)
        observersMap[source] = observer as Observer<in Any?>
    }

    inline fun <reified T : Any?> removeObserver(
        source: LiveData<T>,
        observer: Observer<T>
    ) {
        source.removeObserver(observer)
        observersMap.remove(source)
    }

    fun removeAll() {
        observersMap.forEach { entry ->
            entry.key.removeObserver(entry.value)
        }
        observersMap.clear()
    }
}
