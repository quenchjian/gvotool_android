package me.quenchjian.gvotool.ui.mvvm

sealed interface State<out T> {

  object Busy : State<Nothing>
  object Idle : State<Nothing>
  class Success<out T>(val value: T) : State<T>
  class Error(val t: Throwable) : State<Nothing>

  fun interface Observer<T : Any> {
    fun onStateChange(state: State<T>)
  }
}