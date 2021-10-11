package me.quenchjian.gvotool.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import me.quenchjian.gvotool.database.entity.Character
import me.quenchjian.gvotool.database.entity.Discovery
import me.quenchjian.gvotool.database.entity.DiscoveryState
import me.quenchjian.gvotool.database.entity.DiscoveryStatistics

@Dao
abstract class DiscoveryDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  abstract fun init(discoveries: List<Discovery>)

  @Query("DELETE FROM Discovery")
  abstract fun clear()

  @Insert(onConflict = OnConflictStrategy.ABORT)
  abstract fun insert(discovery: Discovery): Long

  @Update(onConflict = OnConflictStrategy.ABORT)
  abstract fun update(discovery: Discovery): Int

  @Transaction
  @Query("SELECT * FROM DiscoveryState WHERE character_id = :characterId ORDER BY discovery_id")
  abstract fun loadDiscoveryStatistics(characterId: Int): List<DiscoveryStatistics>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insert(states: List<DiscoveryState>)

  @Update(onConflict = OnConflictStrategy.ABORT)
  abstract fun update(state: DiscoveryState): Int

  fun markDiscovered(character: Character, discoveries: List<Discovery>) {
    insert(discoveries.map { DiscoveryState(character.id, it.id, true) })
  }

  fun markUndiscovered(character: Character, discoveries: List<Discovery>) {
    insert(discoveries.map { DiscoveryState(character.id, it.id, false) })
  }

  fun toggleDiscovered(character: Character, discovery: Discovery, discovered: Boolean) {
    update(DiscoveryState(character.id, discovery.id, discovered))
  }
}