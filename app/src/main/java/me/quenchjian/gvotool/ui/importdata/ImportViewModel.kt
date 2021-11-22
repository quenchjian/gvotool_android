package me.quenchjian.gvotool.ui.importdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.quenchjian.gvotool.ui.mvvm.State
import me.quenchjian.gvotool.ui.mvvm.ViewModel
import java.io.InputStream
import javax.inject.Inject

class ImportViewModel @Inject constructor(
  private val importDiscovery: ImportDiscovery,
) : ViewModel() {

  val importState: LiveData<State<Unit>> = MutableLiveData()

  init {

    importDiscovery.addObserver(Observer(importState as MutableLiveData))
  }

  fun importExcel(inputStream: InputStream?) {
    inputStream ?: return
    importDiscovery.invoke(inputStream)
  }

  override fun dispose() {
    importDiscovery.dispose()
  }
}