package me.quenchjian.gvotool.ui.events

import android.os.SystemClock
import android.view.View

private class FastClickListener(
  private val interval: Long = 500L,
  private val wrapped: View.OnClickListener,
) : View.OnClickListener {

  private var lastClick: Long = 0

  override fun onClick(v: View) {
    if (SystemClock.elapsedRealtime() - lastClick < interval) {
      return
    }
    lastClick = SystemClock.elapsedRealtime()
    wrapped.onClick(v)
  }
}

inline fun View.onAttach(crossinline action: (view: View) -> Unit): View {
  addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
    override fun onViewAttachedToWindow(view: View) {
      removeOnAttachStateChangeListener(this)
      action(view)
    }

    override fun onViewDetachedFromWindow(view: View) {}
  })
  return this
}

inline fun View.onDetach(crossinline action: (view: View) -> Unit) {
  addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
    override fun onViewAttachedToWindow(view: View) {}

    override fun onViewDetachedFromWindow(view: View) {
      removeOnAttachStateChangeListener(this)
      action(view)
    }
  })
}

fun View.onClick(click: (View) -> Unit) {
  onDetach { setOnClickListener(null) }
  setOnClickListener(FastClickListener(wrapped = click))
}