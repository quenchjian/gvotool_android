package me.quenchjian.gvotool.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.quenchjian.gvotool.data.Settings
import me.quenchjian.gvotool.ui.mvvm.ViewModel
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val settings: Settings) : ViewModel() {

  val skip: LiveData<Boolean> = MutableLiveData()

  init {
    skip().value = settings.skipped || (settings.characterInitialized && settings.discoveryImported)
  }

  fun skipInitialization() {
    settings.skipped = true
    skip().value = true
  }

  private fun skip() = skip as MutableLiveData
}