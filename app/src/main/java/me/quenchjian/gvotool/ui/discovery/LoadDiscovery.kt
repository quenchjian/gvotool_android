package me.quenchjian.gvotool.ui.discovery

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.withContext
import me.quenchjian.gvotool.concurrent.Dispatcher
import me.quenchjian.gvotool.data.GvoDatabase
import me.quenchjian.gvotool.data.entity.Character
import me.quenchjian.gvotool.data.entity.Discovery
import me.quenchjian.gvotool.data.entity.DiscoveryDetail
import me.quenchjian.gvotool.ui.mvvm.ObservableUseCase
import javax.inject.Inject

class LoadDiscovery @Inject constructor(
  dispatcher: Dispatcher,
  private val db: GvoDatabase,
) : ObservableUseCase<List<DiscoveryDetail>>(dispatcher) {

  @MainThread
  operator fun invoke(character: Character, type: Discovery.Type) = submit { execute(character, type) }

  @WorkerThread
  suspend fun execute(character: Character, type: Discovery.Type): List<DiscoveryDetail> = withContext(dispatcher.io) {
    db.discoveryDao().loadDetail(character.id, type)
  }
}