package me.quenchjian.gvotool.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import me.quenchjian.gvotool.data.entity.Discovery
import me.quenchjian.gvotool.data.entity.DiscoveryDetail
import me.quenchjian.gvotool.data.entity.DiscoveryState
import me.quenchjian.gvotool.data.entity.DiscoveryStatistics

@Dao
abstract class DiscoveryDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  abstract fun init(discoveries: List<Discovery>)

  @Query("DELETE FROM Discovery")
  abstract fun clear()

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  abstract fun insert(discovery: Discovery): Long

  @Update(onConflict = OnConflictStrategy.IGNORE)
  abstract fun update(discovery: Discovery): Int

  @Query("SELECT * FROM Discovery WHERE id = :id")
  abstract fun load(id: Int): Discovery?

  @Query("SELECT * FROM DiscoveryDetail WHERE character_id = :characterId AND discovered = 1")
  abstract fun loadDetail(characterId: Int): List<DiscoveryDetail>

  @Query("SELECT * FROM DiscoveryDetail WHERE character_id = :characterId AND type = :type")
  abstract fun loadDetail(characterId: Int, type: Discovery.Type): List<DiscoveryDetail>

  @Query("SELECT type, count(*) AS total, SUM(CASE WHEN discovered = 1 THEN merit END) AS merit, COUNT(CASE WHEN discovered = 1 THEN 1 END) AS discovered FROM DiscoveryDetail WHERE character_id = :characterId GROUP BY type")
  abstract fun loadStatistics(characterId: Int): List<DiscoveryStatistics>

  @Transaction
  @Query("INSERT OR REPLACE INTO DiscoveryState SELECT :characterId, id, 0 FROM Discovery")
  abstract fun initState(characterId: Int)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insert(states: List<DiscoveryState>)

  @Update(onConflict = OnConflictStrategy.ABORT)
  abstract fun update(state: DiscoveryState): Int
}