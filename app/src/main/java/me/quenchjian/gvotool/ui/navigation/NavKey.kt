package me.quenchjian.gvotool.ui.navigation

import android.os.Parcelable
import com.zhuinden.simplestack.navigator.DefaultViewKey
import com.zhuinden.simplestack.navigator.ViewChangeHandler
import com.zhuinden.simplestack.navigator.changehandlers.FadeViewChangeHandler
import com.zhuinden.simplestack.navigator.changehandlers.NoOpViewChangeHandler
import com.zhuinden.simplestack.navigator.changehandlers.SegueViewChangeHandler

abstract class NavKey(
  private val animation: ScreenAnimation = ScreenAnimation.NONE,
) : DefaultViewKey, Parcelable {

  val name: String = this::class.java.simpleName

  override fun viewChangeHandler(): ViewChangeHandler {
    return when (animation) {
      ScreenAnimation.NONE -> NoOpViewChangeHandler()
      ScreenAnimation.FADE -> FadeViewChangeHandler()
      ScreenAnimation.HORIZONTAL -> SegueViewChangeHandler()
      ScreenAnimation.VERTICAL -> TODO()
    }
  }
}