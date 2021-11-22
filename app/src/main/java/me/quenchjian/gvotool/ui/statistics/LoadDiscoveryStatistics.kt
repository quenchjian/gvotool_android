package me.quenchjian.gvotool.ui.statistics

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.withContext
import me.quenchjian.gvotool.concurrent.Dispatcher
import me.quenchjian.gvotool.data.GvoDatabase
import me.quenchjian.gvotool.data.entity.Character
import me.quenchjian.gvotool.data.entity.DiscoveryStatistics
import me.quenchjian.gvotool.ui.mvvm.ObservableUseCase
import javax.inject.Inject

class LoadDiscoveryStatistics @Inject constructor(
  dispatcher: Dispatcher,
  private val db: GvoDatabase,
) : ObservableUseCase<List<DiscoveryStatistics>>(dispatcher) {

  @MainThread
  operator fun invoke(character: Character) = submit { execute(character) }

  @WorkerThread
  suspend fun execute(character: Character): List<DiscoveryStatistics> = withContext(dispatcher.io) {
    db.discoveryDao().loadStatistics(character.id).sortedBy { it.type.id }
  }
}