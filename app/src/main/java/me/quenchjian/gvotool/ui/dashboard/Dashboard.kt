package me.quenchjian.gvotool.ui.dashboard

import kotlinx.parcelize.Parcelize
import me.quenchjian.gvotool.R
import me.quenchjian.gvotool.ui.navigation.Screen

sealed class Dashboard : Screen() {

  override fun layout(): Int = R.layout.view_dashboard

  @Parcelize
  object StatisticsScreen : Dashboard()

  @Parcelize
  object DiscoveriesScreen : Dashboard()

  @Parcelize
  object ManagementScreen : Dashboard()
}