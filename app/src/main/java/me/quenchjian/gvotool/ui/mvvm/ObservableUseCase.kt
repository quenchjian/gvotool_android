package me.quenchjian.gvotool.ui.mvvm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.quenchjian.gvotool.concurrent.Dispatcher
import timber.log.Timber
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.coroutines.CoroutineContext

abstract class ObservableUseCase<T : Any>(protected val dispatcher: Dispatcher) : CoroutineScope {

  private val lock: Lock = ReentrantLock()
  private val observers = CopyOnWriteArraySet<State.Observer<T>>()

  override val coroutineContext: CoroutineContext = dispatcher.main

  fun addObserver(observer: State.Observer<T>) {
    lock.withLock { observers.add(observer) }
  }

  fun removeObserver(observer: State.Observer<T>) {
    lock.withLock { observers.remove(observer) }
  }

  fun dispose() {
    lock.withLock { observers.clear() }
  }

  protected fun submit(executor: suspend () -> T) {
    launch {
      try {
        notifyStateChange(State.Busy)
        val result = executor()
        notifyStateChange(State.Success(result))
      } catch (t: Throwable) {
        Timber.e(t)
        notifyStateChange(State.Error(t))
      } finally {
        notifyStateChange(State.Idle)
      }
    }
  }

  protected fun notifyStateChange(state: State<T>) {
    getObservers().forEach { it.onStateChange(state) }
  }

  private fun getObservers(): Set<State.Observer<T>> {
    return lock.withLock { observers.toSet() }
  }
}