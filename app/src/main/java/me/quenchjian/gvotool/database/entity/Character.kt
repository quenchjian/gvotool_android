package me.quenchjian.gvotool.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character(
  @PrimaryKey(autoGenerate = true) val id: Int,
  val account: String,
  val name: String,
)