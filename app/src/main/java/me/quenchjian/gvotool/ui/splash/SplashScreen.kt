package me.quenchjian.gvotool.ui.splash

import kotlinx.parcelize.Parcelize
import me.quenchjian.gvotool.R
import me.quenchjian.gvotool.ui.navigation.Screen

@Parcelize
object SplashScreen : Screen() {
  override fun layout(): Int = R.layout.view_splash
}