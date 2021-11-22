package me.quenchjian.gvotool.ui.discovery

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.withContext
import me.quenchjian.gvotool.concurrent.Dispatcher
import me.quenchjian.gvotool.data.GvoDatabase
import me.quenchjian.gvotool.data.entity.DiscoveryDetail
import me.quenchjian.gvotool.data.entity.DiscoveryState
import me.quenchjian.gvotool.ui.mvvm.ObservableUseCase
import javax.inject.Inject

class ToggleDiscoveryState @Inject constructor(
  dispatcher: Dispatcher,
  private val db: GvoDatabase,
) : ObservableUseCase<Unit>(dispatcher) {

  @MainThread
  operator fun invoke(data: List<DiscoveryDetail>) = submit { execute(data) }

  @WorkerThread
  suspend fun execute(data: List<DiscoveryDetail>) = withContext(dispatcher.io) {
    db.discoveryDao().insert(data.map { DiscoveryState(it.characterId, it.discoveryId, it.discovered) })
  }
}