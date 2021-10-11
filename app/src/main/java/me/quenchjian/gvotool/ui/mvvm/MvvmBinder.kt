package me.quenchjian.gvotool.ui.mvvm

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.savedstate.SavedStateRegistry
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.navigator.DefaultStateChanger
import me.quenchjian.gvotool.ui.events.onAttach
import me.quenchjian.gvotool.ui.events.onDetach
import me.quenchjian.gvotool.ui.importdata.ImportScreen
import me.quenchjian.gvotool.ui.importdata.ImportView
import me.quenchjian.gvotool.ui.navigation.Screen
import timber.log.Timber

/**
 * Binder to bind between View and ViewModel
 */
class MvvmBinder(
  private val registry: SavedStateRegistry,
  private val factory: ViewModelFactory,
) : DefaultStateChanger.ViewChangeCompletionListener {

  override fun handleViewChangeComplete(
    stateChange: StateChange,
    container: ViewGroup,
    previousView: View?,
    newView: View,
    completionCallback: DefaultStateChanger.ViewChangeCompletionListener.Callback,
  ) {
    completionCallback.viewChangeComplete()
    val key: Screen = stateChange.topNewKey()
    Timber.tag("MVVM").d("create View and ViewModel for ${key.name}")
    when (key) {
      is ImportScreen -> bind(ImportView(newView, factory.create(key)))
      else -> Unit
    }
  }

  private fun <V : ViewContainer> bind(view: V) {
    val key = view::class.java.name
    view.root.onAttach { view.attach(registry.consumeRestoredStateForKey(key) ?: Bundle.EMPTY) }
    view.root.onDetach { registry.registerSavedStateProvider(key) { view.detach() } }
  }
}