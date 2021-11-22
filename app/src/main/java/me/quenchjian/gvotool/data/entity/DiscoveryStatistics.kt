package me.quenchjian.gvotool.data.entity

data class DiscoveryStatistics(
  val type: Discovery.Type,
  val total: Int,
  val merit: Int,
  val discovered: Int,
)