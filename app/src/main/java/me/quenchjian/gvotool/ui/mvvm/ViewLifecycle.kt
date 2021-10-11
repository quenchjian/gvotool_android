package me.quenchjian.gvotool.ui.mvvm

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import me.quenchjian.gvotool.ui.events.onAttach
import me.quenchjian.gvotool.ui.events.onDetach
import timber.log.Timber

class ViewLifecycle(view: View) : LifecycleOwner {

  private val lifecycleRegistry = LifecycleRegistry(this)

  init {
    lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    view.onAttach {
      Timber.tag("MVVM").d("onViewAttachedToWindow")
      lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }
    view.onDetach {
      Timber.tag("MVVM").d("onViewDetachedFromWindow")
      lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }
  }

  override fun getLifecycle(): Lifecycle = lifecycleRegistry
}