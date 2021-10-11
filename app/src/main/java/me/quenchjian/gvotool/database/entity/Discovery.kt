package me.quenchjian.gvotool.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Discovery(
  @PrimaryKey val id: Int,
  val type: Type,
  val name: String,
  val task: String = "",
  @ColumnInfo(name = "task_ref") val taskRef: String = "",
  val star: Int = 0,
  val card: Int = 0,
  val medal: Int = 0,
  val version: String = "",
) {

  enum class Type {
    HISTORIC_SITE,
    RELIGIOUS_ARCHITECTURE,
    HISTORICAL_RELIC,
    RELIGIOUS_RELIC,
    ARTWORK,
    TREASURE,
    FOSSIL,
    PLANT,
    INSECT,
    BIRD,
    SMALL_CREATURE,
    MEDIUM_CREATURE,
    LARGE_CREATURE,
    MARINE_LIFE,
    PORT_SETTLEMENT,
    GEOGRAPHY,
    ASTRONOMY,
    WEATHER_PHENOMENA,
    FOLKLORE
  }
}