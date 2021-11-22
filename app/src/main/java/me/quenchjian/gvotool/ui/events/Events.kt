package me.quenchjian.gvotool.ui.events

import android.os.SystemClock
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

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
  if (isAttachedToWindow) {
    action(this)
    return this
  }
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
  setOnClickListener(FastClickListener(wrapped = click))
}

fun RecyclerView.onScrollDown(action: () -> Unit) {
  addOnScrollListener(Scroll.DOWN.wrap(0, action))
}

fun RecyclerView.onScrollToBottom(threshold: Int = 0, action: () -> Unit) {
  addOnScrollListener(Scroll.REACH_BOTTOM.wrap(threshold, action))
}

fun RecyclerView.onScrollToTop(threshold: Int = 0, action: () -> Unit) {
  addOnScrollListener(Scroll.REACH_TOP.wrap(threshold, action))
}

fun RecyclerView.onScrollUp(action: () -> Unit) {
  addOnScrollListener(Scroll.UP.wrap(0, action))
}

enum class Scroll {
  REACH_BOTTOM, REACH_TOP, UP, DOWN;

  fun wrap(threshold: Int = 0, action: () -> Unit): RecyclerView.OnScrollListener {
    return ScrollListener(threshold) { scroll ->
      if (scroll == this) {
        action()
      }
    }
  }
}

private class ScrollListener(
  private val threshold: Int = 0,
  private val onScroll: (Scroll) -> Unit,
) : RecyclerView.OnScrollListener() {

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    val lm = recyclerView.layoutManager ?: return
    val adapter = recyclerView.adapter ?: return
    when (lm) {
      is GridLayoutManager -> onScrolled(lm, adapter.itemCount, dy)
      is LinearLayoutManager -> {
        val horizontal = lm.orientation == RecyclerView.HORIZONTAL
        onScrolled(lm, adapter.itemCount, if (horizontal) dx else dy)
      }
      is StaggeredGridLayoutManager -> onScrolled(lm, adapter.itemCount, dy)
    }
  }

  private fun onScrolled(lm: LinearLayoutManager, itemCount: Int, delta: Int) {
    if (delta == 0) return
    val first = lm.findFirstCompletelyVisibleItemPosition()
    val last = lm.findLastVisibleItemPosition()
    when {
      delta > 0 && (itemCount - last <= threshold) -> onScroll(Scroll.REACH_BOTTOM)
      delta > 0 -> onScroll(Scroll.DOWN)
      delta < 0 && first <= threshold -> onScroll(Scroll.REACH_TOP)
      delta < 0 -> onScroll(Scroll.UP)
    }
  }

  private fun onScrolled(lm: StaggeredGridLayoutManager, itemCount: Int, delta: Int) {
    if (delta == 0) return
    val column = lm.spanCount - 1
    val first = intArrayOf()
    val last = intArrayOf()
    lm.findFirstCompletelyVisibleItemPositions(first)
    lm.findLastVisibleItemPositions(last)
    when {
      delta > 0 && (itemCount - last[column] <= threshold) -> onScroll(Scroll.REACH_BOTTOM)
      delta > 0 -> onScroll(Scroll.DOWN)
      delta < 0 && first[0] <= threshold -> onScroll(Scroll.REACH_TOP)
      delta < 0 -> onScroll(Scroll.UP)
    }
  }
}