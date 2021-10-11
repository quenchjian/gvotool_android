package me.quenchjian.gvotool.ui.mvvm

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner

/**
 * View for MVVM architecture
 */
abstract class ViewContainer(val root: View) : LifecycleOwner by ViewLifecycle(root) {

  val context: Context = root.context

  open fun attach(savedState: Bundle) {}
  open fun detach(): Bundle = Bundle.EMPTY
}