package me.quenchjian.gvotool.ui.mvvm

import me.quenchjian.gvotool.ui.importdata.ImportScreen
import me.quenchjian.gvotool.ui.importdata.ImportViewModel
import me.quenchjian.gvotool.ui.navigation.NavKey
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
  private val importVM: Provider<ImportViewModel>,
) {

  @Suppress("unchecked_cast")
  fun <T : ViewModel> create(key: NavKey): T {
    return when (key) {
      is ImportScreen -> importVM.get()
      else -> throw IllegalArgumentException("unknown $key to create")
    } as T
  }
}