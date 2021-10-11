package me.quenchjian.gvotool.database

import androidx.room.Database
import androidx.room.RoomDatabase
import me.quenchjian.gvotool.database.dao.DiscoveryDao
import me.quenchjian.gvotool.database.entity.Character
import me.quenchjian.gvotool.database.entity.Discovery
import me.quenchjian.gvotool.database.entity.DiscoveryState

@Database(version = 1, entities = [Character::class, Discovery::class, DiscoveryState::class])
abstract class GvoDatabase : RoomDatabase() {
  abstract fun discoveryDao(): DiscoveryDao
}