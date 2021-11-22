package me.quenchjian.gvotool.data.entity

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(
  "SELECT C.id AS character_id, C.name AS character_name," +
      " D.id AS discovery_id, D.type, D.name, D.name, D.task, D.task_ref, D.star, D.card, D.merit, D.version," +
      " DS.discovered" +
      " FROM DiscoveryState AS DS" +
      " INNER JOIN Character AS C ON DS.character_id = C.id" +
      " INNER JOIN Discovery AS D ON DS.discovery_id = D.id"
)
data class DiscoveryDetail(
  @ColumnInfo(name = "character_id") val characterId: Int,
  @ColumnInfo(name = "character_name") val characterName: Int,
  @ColumnInfo(name = "discovery_id") val discoveryId: Int,
  val type: Discovery.Type,
  val name: String,
  val task: String = "",
  @ColumnInfo(name = "task_ref") val taskRef: String = "",
  val star: Int = 0,
  val card: Int = 0,
  val merit: Int = 0,
  val version: String = "",
  val discovered: Boolean,
) {

  fun compareWith(other: DiscoveryDetail): Boolean {
    return characterId == other.characterId && discoveryId == other.discoveryId
  }
}