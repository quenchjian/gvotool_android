package me.quenchjian.gvotool.ui.mvvm

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.zhuinden.simplestack.navigator.Navigator

/**
 * View for MVVM architecture
 */
abstract class ViewContainer<VM : ViewModel>(
  val root: View,
  val vm: VM,
) : LifecycleOwner by ViewLifecycle(root) {

  val context: Context = root.context
  val backstack = Navigator.getBackstack(context)

  open fun attach(savedState: Bundle) {}
  open fun detach(): Bundle = Bundle.EMPTY
}