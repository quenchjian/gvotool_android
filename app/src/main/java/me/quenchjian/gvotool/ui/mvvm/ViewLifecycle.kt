package me.quenchjian.gvotool.ui.mvvm

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import me.quenchjian.gvotool.ui.events.onAttach
import me.quenchjian.gvotool.ui.events.onDetach

class ViewLifecycle(view: View) : LifecycleOwner {

  private val lifecycleRegistry = LifecycleRegistry(this)

  init {
    lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    view.onAttach { lifecycleRegistry.currentState = Lifecycle.State.STARTED }
    view.onDetach { lifecycleRegistry.currentState = Lifecycle.State.DESTROYED }
  }

  override fun getLifecycle(): Lifecycle = lifecycleRegistry
}