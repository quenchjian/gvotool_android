package me.quenchjian.gvotool.ui.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.quenchjian.gvotool.data.entity.Character
import me.quenchjian.gvotool.ui.mvvm.State
import me.quenchjian.gvotool.ui.mvvm.ViewModel
import javax.inject.Inject

class CharacterViewModel @Inject constructor(
  private val loadCharacters: LoadCharacters,
  private val addEditCharacter: AddEditCharacter,
) : ViewModel() {

  val loadState: LiveData<State<List<Character>>> = MutableLiveData()
  val addEditState: LiveData<State<Character>> = MutableLiveData()

  init {
    loadCharacters.addObserver(Observer(loadState as MutableLiveData))
    addEditCharacter.addObserver(Observer(addEditState as MutableLiveData))
  }

  fun loadCharacters() {
    loadCharacters.invoke()
  }

  fun addCharacter(form: CharacterForm) {
    addEditCharacter.invoke(form)
  }

  override fun dispose() {
    loadCharacters.dispose()
    addEditCharacter.dispose()
  }
}