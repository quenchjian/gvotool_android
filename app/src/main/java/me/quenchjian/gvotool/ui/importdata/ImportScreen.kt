package me.quenchjian.gvotool.ui.importdata

import kotlinx.parcelize.Parcelize
import me.quenchjian.gvotool.R
import me.quenchjian.gvotool.ui.navigation.NavKey

@Parcelize
class ImportScreen : NavKey() {

  override fun layout(): Int = R.layout.view_importexcel
}