package me.quenchjian.gvotool.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["name", "star", "merit"])])
data class Discovery(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val type: Type,
  val name: String,
  val task: String = "",
  @ColumnInfo(name = "task_ref") val taskRef: String = "",
  val star: Int = 0,
  val card: Int = 0,
  val merit: Int = 0,
  val version: String = "",
) {

  enum class Type(val id: Int) {
    HISTORIC_SITE(0),
    RELIGIOUS_ARCHITECTURE(1),
    HISTORICAL_RELIC(2),
    RELIGIOUS_RELIC(3),
    ARTWORK(4),
    TREASURE(5),
    FOSSIL(6),
    PLANT(7),
    INSECT(8),
    BIRD(9),
    SMALL_CREATURE(10),
    MEDIUM_CREATURE(11),
    LARGE_CREATURE(12),
    MARINE_LIFE(13),
    PORT_SETTLEMENT(14),
    GEOGRAPHY(15),
    ASTRONOMY(16),
    WEATHER_PHENOMENA(17),
    FOLKLORE(18),
  }
}