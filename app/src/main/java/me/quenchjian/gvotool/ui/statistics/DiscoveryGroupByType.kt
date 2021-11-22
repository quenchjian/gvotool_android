package me.quenchjian.gvotool.ui.statistics

import me.quenchjian.gvotool.data.entity.Discovery

data class DiscoveryGroupByType(val type: Discovery.Type, val total: Int, val discovered: Int)