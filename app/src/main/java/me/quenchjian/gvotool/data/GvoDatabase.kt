package me.quenchjian.gvotool.data

import androidx.room.Database
import androidx.room.RoomDatabase
import me.quenchjian.gvotool.data.dao.CharacterDao
import me.quenchjian.gvotool.data.dao.DiscoveryDao
import me.quenchjian.gvotool.data.entity.Character
import me.quenchjian.gvotool.data.entity.Discovery
import me.quenchjian.gvotool.data.entity.DiscoveryDetail
import me.quenchjian.gvotool.data.entity.DiscoveryState

@Database(version = 1,
  entities = [Character::class, Discovery::class, DiscoveryState::class],
  views = [DiscoveryDetail::class]
)
abstract class GvoDatabase : RoomDatabase() {
  abstract fun characterDao(): CharacterDao
  abstract fun discoveryDao(): DiscoveryDao
}