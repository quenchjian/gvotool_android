package me.quenchjian.gvotool.ui.mvvm

import androidx.lifecycle.MutableLiveData

/**
 * ViewModel for MVVM architecture
 */
abstract class ViewModel : androidx.lifecycle.ViewModel() {
  open fun setup() {}
  open fun dispose() {}

  open class Observer<T : Any>(private val liveData: MutableLiveData<State<T>>) : State.Observer<T> {
    override fun onStateChange(state: State<T>) {
      liveData.value = state
    }
  }
}