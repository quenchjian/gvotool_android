package me.quenchjian.gvotool.ui.widgets

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.quenchjian.gvotool.ui.events.onClick

abstract class RecyclerViewHolder<T>(root: View) : RecyclerView.ViewHolder(root) {
  abstract fun bind(item: T)
}

abstract class RecyclerListAdapter<T, VH : RecyclerView.ViewHolder>(
  compareItem: (old: T, new: T) -> Boolean,
  compareContent: (old: T, new: T) -> Boolean,
  private val viewHolderCreator: (parent: ViewGroup, viewType: Int) -> VH,
) : ListAdapter<T, VH>(object : DiffUtil.ItemCallback<T>() {
  override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = compareItem.invoke(oldItem, newItem)
  override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = compareContent.invoke(oldItem, newItem)
}) {

  private var itemClick: (T) -> Unit = {}

  final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    return viewHolderCreator.invoke(parent, viewType).also { onViewHolderCreated(it) }
  }

  @CallSuper
  open fun onViewHolderCreated(holder: VH) {
    holder.itemView.onClick { itemClick.invoke(getItem(holder.adapterPosition)) }
  }

  final override fun onBindViewHolder(holder: VH, position: Int) {
    val item = getItem(position)
    if (holder is RecyclerViewHolder<*>) {
      @Suppress("unchecked_cast")
      (holder as RecyclerViewHolder<T>).bind(item)
    } else {
      onBindViewHolder(holder, item)
    }
  }

  open fun onBindViewHolder(holder: VH, item: T) {}

  fun add(item: T) {
    val data = currentList.toMutableList()
    data.add(item)
    submitList(data)
  }

  fun set(id: Int, item: T) {
    val data = currentList.toMutableList()
    data[id] = item
    submitList(data)
  }

  open fun indexOf(item: T): Int = -1

  fun onItemClick(click: (T) -> Unit) = apply { itemClick = click }
}

fun RecyclerView.saveScrollState(state: Bundle, key: String = "key-scroll-state-${this.id}") {
  val lm = layoutManager ?: return
  state.putParcelable(key, lm.onSaveInstanceState())
}

fun RecyclerView.restoreScrollState(state: Bundle, key: String = "key-scroll-state-${this.id}") {
  val lm = layoutManager ?: return
  val adapter = adapter ?: return
  val saved = state.getParcelable<Parcelable>(key) ?: return
  if (adapter.itemCount > 0) {
    lm.onRestoreInstanceState(saved)
  } else {
    adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
      override fun onItemRangeChanged(positionStart: Int, itemCount: Int) = restore()
      override fun onChanged() = restore()
      private fun restore() {
        adapter.unregisterAdapterDataObserver(this)
        lm.onRestoreInstanceState(saved)
      }
    })
  }
}