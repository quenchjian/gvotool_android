package me.quenchjian.gvotool.ui.widgets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun View.show() {
  visibility = View.VISIBLE
}

fun View.showIf(condition: () -> Boolean) {
  if (condition()) show()
}

fun View.hide(keep: Boolean = false) {
  visibility = if (keep) View.INVISIBLE else View.GONE
}

fun View.hideIf(keep: Boolean = false, condition: () -> Boolean) {
  if (condition()) hide(keep)
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
  return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}