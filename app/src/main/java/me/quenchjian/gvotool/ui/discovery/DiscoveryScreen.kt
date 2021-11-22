package me.quenchjian.gvotool.ui.discovery

import kotlinx.parcelize.Parcelize
import me.quenchjian.gvotool.R
import me.quenchjian.gvotool.data.entity.Discovery
import me.quenchjian.gvotool.ui.navigation.Screen

@Parcelize
data class DiscoveryScreen(val type: Discovery.Type) : Screen() {

  override fun layout(): Int = R.layout.view_discovery
}