package me.quenchjian.gvotool.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["server", "name"], unique = true)])
data class Character(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val account: String,
  val server: String,
  val name: String,
)