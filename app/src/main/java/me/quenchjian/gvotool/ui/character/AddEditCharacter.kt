package me.quenchjian.gvotool.ui.character

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.withContext
import me.quenchjian.gvotool.concurrent.Dispatcher
import me.quenchjian.gvotool.data.GvoDatabase
import me.quenchjian.gvotool.data.Settings
import me.quenchjian.gvotool.data.entity.Character
import me.quenchjian.gvotool.ui.mvvm.ObservableUseCase
import javax.inject.Inject

class AddEditCharacter @Inject constructor(
  dispatcher: Dispatcher,
  private val db: GvoDatabase,
  private val settings: Settings,
) : ObservableUseCase<Character>(dispatcher) {

  @MainThread
  operator fun invoke(form: CharacterForm) = submit { execute(form) }

  @WorkerThread
  suspend fun execute(form: CharacterForm): Character = withContext(dispatcher.io) {
    form.checkNotEmpty()
    when (val saved = db.characterDao().queryByNameAndServer(form.server, form.name)) {
      null -> form.toCharacter().also {
        if (db.characterDao().insert(it) == -1L) {
          throw InsertConflictException
        }
        settings.characterInitialized = true
      }
      else -> when {
        form.equalTo(saved) -> saved
        else -> saved.copy(account = form.account, server = form.server, name = form.name).also {
          if (db.characterDao().update(it) == -1) {
            throw UpdateConflictException
          }
        }
      }
    }
  }
}