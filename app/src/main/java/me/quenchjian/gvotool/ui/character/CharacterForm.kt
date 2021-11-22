package me.quenchjian.gvotool.ui.character

import android.database.SQLException
import me.quenchjian.gvotool.data.entity.Character

data class CharacterForm(val account: String, val server: String, val name: String) {

  fun checkNotEmpty() {
    when {
      account.isEmpty() -> throw AccountEmpty
      server.isEmpty() -> throw ServerEmpty
      name.isEmpty() -> throw NameEmpty
      else -> Unit
    }
  }

  fun equalTo(character: Character): Boolean {
    return account == character.account && server == character.server && name == character.name
  }

  fun toCharacter() = Character(account = account, server = server, name = name)
}

object AccountEmpty : IllegalArgumentException()
object ServerEmpty : IllegalArgumentException()
object NameEmpty : IllegalArgumentException()
object CharacterExisted : IllegalStateException()
object InsertConflictException : SQLException()
object UpdateConflictException : SQLException()