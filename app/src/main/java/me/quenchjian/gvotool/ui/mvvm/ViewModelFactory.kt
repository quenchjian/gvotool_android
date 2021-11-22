package me.quenchjian.gvotool.ui.mvvm

import me.quenchjian.gvotool.ui.character.CharacterViewModel
import me.quenchjian.gvotool.ui.discovery.DiscoveryViewModel
import me.quenchjian.gvotool.ui.importdata.ImportViewModel
import me.quenchjian.gvotool.ui.splash.SplashViewModel
import me.quenchjian.gvotool.ui.statistics.StatisticsViewModel
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class ViewModelFactory @Inject constructor(
  private val splashVM: Provider<SplashViewModel>,
  private val characterVM: Provider<CharacterViewModel>,
  private val importVM: Provider<ImportViewModel>,
  private val statisticsVM: Provider<StatisticsViewModel>,
  private val discoveryVM: Provider<DiscoveryViewModel>,
) {

  @Suppress("unchecked_cast")
  fun <T : ViewModel> create(klz: KClass<T>): T = when (klz) {
    SplashViewModel::class -> splashVM.get()
    CharacterViewModel::class -> characterVM.get()
    ImportViewModel::class -> importVM.get()
    StatisticsViewModel::class -> statisticsVM.get()
    DiscoveryViewModel::class -> discoveryVM.get()
    else -> throw IllegalArgumentException("unknown $klz to create")
  } as T
}