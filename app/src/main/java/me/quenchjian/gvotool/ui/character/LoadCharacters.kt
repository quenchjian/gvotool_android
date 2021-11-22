package me.quenchjian.gvotool.ui.character

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.withContext
import me.quenchjian.gvotool.concurrent.Dispatcher
import me.quenchjian.gvotool.data.GvoDatabase
import me.quenchjian.gvotool.data.entity.Character
import me.quenchjian.gvotool.ui.mvvm.ObservableUseCase
import javax.inject.Inject

class LoadCharacters @Inject constructor(
  dispatcher: Dispatcher,
  private val db: GvoDatabase,
) : ObservableUseCase<List<Character>>(dispatcher) {

  @MainThread
  operator fun invoke() = submit { execute() }

  @WorkerThread
  suspend fun execute(): List<Character> = withContext(dispatcher.io) {
    db.characterDao().queryAll()
  }
}