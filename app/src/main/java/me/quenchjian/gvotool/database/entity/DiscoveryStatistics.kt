package me.quenchjian.gvotool.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class DiscoveryStatistics(
  @Embedded val state: DiscoveryState,
  @Relation(
    entity = Character::class,
    entityColumn = "id",
    parentColumn = "character_id"
  )
  val character: Character,
  @Relation(
    entity = Discovery::class,
    entityColumn = "id",
    parentColumn = "discovery_id"
  )
  val discovery: Discovery,
)