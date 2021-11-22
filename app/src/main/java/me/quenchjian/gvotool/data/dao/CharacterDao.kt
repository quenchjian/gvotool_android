package me.quenchjian.gvotool.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import me.quenchjian.gvotool.data.entity.Character

@Dao
abstract class CharacterDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  abstract fun insert(character: Character): Long

  @Update(onConflict = OnConflictStrategy.ABORT)
  abstract fun update(character: Character): Int

  @Query("SELECT * FROM Character WHERE server = :server AND name = :name")
  abstract fun queryByNameAndServer(server: String, name: String): Character?

  @Query("SELECT * FROM Character")
  abstract fun queryAll(): List<Character>
}