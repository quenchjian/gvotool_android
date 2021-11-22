package me.quenchjian.gvotool.ui.character

import kotlinx.parcelize.Parcelize
import me.quenchjian.gvotool.R
import me.quenchjian.gvotool.ui.navigation.Screen

@Parcelize
object CharacterScreen : Screen() {
  override fun layout(): Int = R.layout.view_addcharacter
}