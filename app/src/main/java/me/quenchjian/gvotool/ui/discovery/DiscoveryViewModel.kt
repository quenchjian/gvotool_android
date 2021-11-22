package me.quenchjian.gvotool.ui.discovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.scopes.ActivityScoped
import me.quenchjian.gvotool.data.Settings
import me.quenchjian.gvotool.data.entity.Character
import me.quenchjian.gvotool.data.entity.Discovery
import me.quenchjian.gvotool.data.entity.DiscoveryDetail
import me.quenchjian.gvotool.ui.character.LoadCharacters
import me.quenchjian.gvotool.ui.dashboard.DashboardViewModel
import me.quenchjian.gvotool.ui.mvvm.State
import javax.inject.Inject

@ActivityScoped
class DiscoveryViewModel @Inject constructor(
  settings: Settings,
  loadCharacters: LoadCharacters,
  private val loadDiscovery: LoadDiscovery,
  private val toggleDiscoveryState: ToggleDiscoveryState,
) : DashboardViewModel(settings, loadCharacters) {

  val loadState: LiveData<State<List<DiscoveryDetail>>> = MutableLiveData()
  val updateState: LiveData<State<Unit>> = MutableLiveData()

  private val loadObserver = Observer(loadState as MutableLiveData)
  private val updateObserver = Observer(updateState as MutableLiveData)

  override fun setup() {
    loadDiscovery.addObserver(loadObserver)
    toggleDiscoveryState.addObserver(updateObserver)
  }

  fun load(character: Character, type: Discovery.Type) {
    loadDiscovery.invoke(character, type)
  }

  fun update(data: List<DiscoveryDetail>) {
    toggleDiscoveryState(data)
  }

  override fun dispose() {
    super.dispose()
    loadDiscovery.dispose()
  }
}