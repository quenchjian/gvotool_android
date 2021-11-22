package me.quenchjian.gvotool.ui.dashboard

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.quenchjian.gvotool.data.Settings
import me.quenchjian.gvotool.data.entity.Character
import me.quenchjian.gvotool.ui.character.LoadCharacters
import me.quenchjian.gvotool.ui.mvvm.State
import me.quenchjian.gvotool.ui.mvvm.ViewModel

abstract class DashboardViewModel(
  private val settings: Settings,
  private val loadCharacters: LoadCharacters,
) : ViewModel() {

  val characters = mutableListOf<Character>()
  val character: LiveData<Character> = MutableLiveData()

  init {
    loadCharacters.addObserver { state ->
      if (state is State.Success && characters.isEmpty()) {
        characters.addAll(state.value)
        selectCharacter(settings.selectedCharacter ?: characters.first())
      }
    }
    loadCharacters.invoke()
  }

  @CallSuper
  open fun selectCharacter(character: Character) {
    (this.character as MutableLiveData).value = character
    settings.selectedCharacter = character
  }

  @CallSuper
  override fun dispose() {
    super.dispose()
    loadCharacters.dispose()
  }
}