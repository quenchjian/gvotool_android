package me.quenchjian.gvotool.ui.mvvm

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.savedstate.SavedStateRegistry
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.navigator.DefaultStateChanger
import me.quenchjian.gvotool.ui.character.CharacterScreen
import me.quenchjian.gvotool.ui.character.CharacterView
import me.quenchjian.gvotool.ui.character.CharacterViewModel
import me.quenchjian.gvotool.ui.dashboard.Dashboard
import me.quenchjian.gvotool.ui.discovery.DiscoveriesView
import me.quenchjian.gvotool.ui.discovery.DiscoveryScreen
import me.quenchjian.gvotool.ui.discovery.DiscoveryView
import me.quenchjian.gvotool.ui.discovery.DiscoveryViewModel
import me.quenchjian.gvotool.ui.events.onAttach
import me.quenchjian.gvotool.ui.events.onDetach
import me.quenchjian.gvotool.ui.importdata.ImportScreen
import me.quenchjian.gvotool.ui.importdata.ImportView
import me.quenchjian.gvotool.ui.importdata.ImportViewModel
import me.quenchjian.gvotool.ui.navigation.Screen
import me.quenchjian.gvotool.ui.splash.SplashScreen
import me.quenchjian.gvotool.ui.splash.SplashView
import me.quenchjian.gvotool.ui.splash.SplashViewModel
import me.quenchjian.gvotool.ui.statistics.StatisticsView
import me.quenchjian.gvotool.ui.statistics.StatisticsViewModel

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
    bind(stateChange.topNewKey(), newView)
  }

  fun bind(key: Screen, view: View) {
    val v = when (key) {
      is SplashScreen -> SplashView(view, factory.create(SplashViewModel::class), this::bind)
      is CharacterScreen -> CharacterView(view, factory.create(CharacterViewModel::class))
      is ImportScreen -> ImportView(view, factory.create(ImportViewModel::class))
      is Dashboard.StatisticsScreen -> StatisticsView(view, factory.create(StatisticsViewModel::class))
      is Dashboard.DiscoveriesScreen -> DiscoveriesView(view, factory.create(DiscoveryViewModel::class), this::bind)
      is DiscoveryScreen -> DiscoveryView(view, key.type, factory.create(DiscoveryViewModel::class))
      else -> throw IllegalArgumentException("Unknown $key to create view controller")
    }
    val stateKey = "$key-${v::class.java.name}"
    v.root.onAttach {
      v.attach(registry.consumeRestoredStateForKey(stateKey) ?: Bundle.EMPTY)
      v.vm.setup()
    }
    v.root.onDetach {
      registry.unregisterSavedStateProvider(stateKey)
      registry.registerSavedStateProvider(stateKey) { v.detach() }
      v.vm.dispose()
    }
  }
}