// package com.nafanya.mp3world.features.dragging
//  
//  import androidx.annotation.CallSuper
//  import androidx.lifecycle.LiveData
//  import androidx.lifecycle.MutableLiveData
//  import com.nafanya.mp3world.core.listUtils.recycler.BaseListItem
//  
//  abstract class StateMachineDraggable<DU, LI : BaseListItem>(
//      dataSource: LiveData<List<DU>?>
//  ) : ListStateMachine<DU, LI>(
//      dataSource
//  ) {
//  
//      protected val mInDraggableMode = MutableLiveData(false)
//      val inDraggableMode: LiveData<Boolean>
//          get() = mInDraggableMode
//  
//      private var isTriggerInit = false
//      private var isConfirming = false
//  
//      @CallSuper
//      open fun moveToDraggableState() {
//          mInDraggableMode.value = true
//          isConfirming = false
//      }
//  
//      @CallSuper
//      open fun moveFromDraggableState() {
//          mInDraggableMode.value = false
//      }
//  
//      @CallSuper
//      open fun confirmChanges() {
//          isConfirming = true
//          moveFromDraggableState()
//      }
//  
//      init {
//          addDataUpdateTrigger(
//              mInDraggableMode,
//              UpdateTriggerPolicy {
//                  if (isTriggerInit && !isConfirming) {
//                      true
//                  } else {
//                      isTriggerInit = true
//                      false
//                  }
//              }
//          )
//      }
//  }
