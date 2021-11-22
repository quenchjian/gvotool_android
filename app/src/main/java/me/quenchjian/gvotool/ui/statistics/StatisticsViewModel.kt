package me.quenchjian.gvotool.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.quenchjian.gvotool.data.Settings
import me.quenchjian.gvotool.data.entity.Character
import me.quenchjian.gvotool.data.entity.DiscoveryStatistics
import me.quenchjian.gvotool.ui.character.LoadCharacters
import me.quenchjian.gvotool.ui.dashboard.DashboardViewModel
import me.quenchjian.gvotool.ui.mvvm.State
import javax.inject.Inject

class StatisticsViewModel @Inject constructor(
  settings: Settings,
  loadCharacters: LoadCharacters,
  private val loadDiscoveryStatistics: LoadDiscoveryStatistics,
) : DashboardViewModel(settings, loadCharacters) {

  val medalRank: LiveData<MedalRank> = MutableLiveData()
  val discoveryState: LiveData<State<List<DiscoveryStatistics>>> = MutableLiveData()

  init {
    loadDiscoveryStatistics.addObserver(object :
      Observer<List<DiscoveryStatistics>>(discoveryState as MutableLiveData) {
      override fun onStateChange(state: State<List<DiscoveryStatistics>>) {
        super.onStateChange(state)
        if (state is State.Success) {
          calculateRank(state.value.sumOf { it.merit })
        }
      }
    })
  }

  override fun selectCharacter(character: Character) {
    super.selectCharacter(character)
    loadDiscoveryStatistics.invoke(character)
  }

  override fun dispose() {
    super.dispose()
    loadDiscoveryStatistics.dispose()
  }

  private fun calculateRank(medal: Int) {
    medalRank as MutableLiveData
    for ((threshold, rank) in nobelRank.asReversed()) {
      if (medal - threshold >= 0) {
        medalRank.value = MedalRank(medal, rank)
        break
      }
    }
    medalRank.value = MedalRank(medal, 0)
  }

  private val nobelRank = listOf(
    MedalRank(1000, 1),
    MedalRank(4000, 2),
    MedalRank(10000, 3),
    MedalRank(25000, 4),
    MedalRank(50000, 5),
    MedalRank(100000, 6),
    MedalRank(150000, 7),
    MedalRank(200000, 8),
    MedalRank(300000, 9),
    MedalRank(400000, 10),
    MedalRank(500000, 11),
    MedalRank(600000, 12),
    MedalRank(700000, 13),
    MedalRank(800000, 14),
    MedalRank(900000, 15),
    MedalRank(1000000, 16),
  )
}