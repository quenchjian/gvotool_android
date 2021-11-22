package me.quenchjian.gvotool.ui.discovery

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.withContext
import me.quenchjian.gvotool.concurrent.Dispatcher
import me.quenchjian.gvotool.data.GvoDatabase
import me.quenchjian.gvotool.data.entity.Discovery
import me.quenchjian.gvotool.data.entity.DiscoveryState
import me.quenchjian.gvotool.ui.mvvm.ObservableUseCase
import javax.inject.Inject

class AddEditDiscovery @Inject constructor(
  dispatcher: Dispatcher,
  private val db: GvoDatabase,
) : ObservableUseCase<Discovery>(dispatcher) {

  @MainThread
  operator fun invoke(discovery: Discovery) = submit { execute(discovery) }

  @WorkerThread
  suspend fun execute(discovery: Discovery): Discovery = withContext(dispatcher.io) {
    when (db.discoveryDao().insert(discovery)) {
      -1L -> if (db.discoveryDao().update(discovery) == -1) {
        throw UpdateConflictException
      }
      else -> {
        val states = db.characterDao().queryAll().map { DiscoveryState(it.id, discovery.id, false) }
        db.discoveryDao().insert(states)
      }
    }
    discovery
  }
}