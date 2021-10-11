package me.quenchjian.gvotool.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["character_id", "discovery_id"])
data class DiscoveryState(
  @ColumnInfo(name = "character_id") val characterId: Int,
  @ColumnInfo(name = "discovery_id") val discoveryId: Int,
  val discovered: Boolean,
)