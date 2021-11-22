package me.quenchjian.gvotool.ui.discovery

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import me.quenchjian.gvotool.R
import me.quenchjian.gvotool.data.entity.Discovery
import me.quenchjian.gvotool.data.entity.DiscoveryDetail
import me.quenchjian.gvotool.databinding.ItemDiscoveryBinding
import me.quenchjian.gvotool.databinding.ViewDiscoveryBinding
import me.quenchjian.gvotool.openBrowser
import me.quenchjian.gvotool.ui.events.onClick
import me.quenchjian.gvotool.ui.mvvm.State
import me.quenchjian.gvotool.ui.mvvm.ViewContainer
import me.quenchjian.gvotool.ui.widgets.LoadingDialog
import me.quenchjian.gvotool.ui.widgets.RecyclerListAdapter
import me.quenchjian.gvotool.ui.widgets.RecyclerViewHolder
import me.quenchjian.gvotool.ui.widgets.inflate

class DiscoveryView(
  root: View,
  private val type: Discovery.Type,
  vm: DiscoveryViewModel,
) : ViewContainer<DiscoveryViewModel>(root, vm) {

  private val view = ViewDiscoveryBinding.bind(root)
  private val adapter: Adapter get() = view.recyclerDiscovery.adapter as Adapter
  private var loadingDialog: LoadingDialog? = null

  init {
    view.recyclerDiscovery.adapter = Adapter()
      .onTaskClick { context.openBrowser(it) }
      .onCheckClick { view.checkboxHeaderName.isChecked = adapter.isAllChecked() }
    view.recyclerDiscovery.layoutManager = LinearLayoutManager(context)
    view.checkboxHeaderName.onClick { adapter.toggleCheckAll(view.checkboxHeaderName.isChecked) }
    view.buttonSave.onClick { vm.update(adapter.currentList) }
  }

  init {
    vm.character.observe(this) { vm.load(it, type) }
    vm.loadState.observe(this) { it.render { data -> adapter.submitList(data) } }
    vm.updateState.observe(this) { it.render() }
  }

  private fun <T> State<T>.render(successor: (T) -> Unit = {}) {
    when (this) {
      is State.Busy -> toggleLoading(true)
      is State.Idle -> toggleLoading(false)
      is State.Success -> successor(this.value)
      is State.Error -> showError(this.t)
    }
  }

  private fun toggleLoading(active: Boolean) {
    val dialog = loadingDialog ?: LoadingDialog(context).also { loadingDialog = it }
    if (active) dialog.show() else dialog.dismiss()
  }

  private fun showError(t: Throwable) {
    val msg = t.message ?: context.getString(R.string.error_unknown)
    Snackbar.make(root, msg, Snackbar.LENGTH_SHORT).show()
  }

  private class Holder(root: View) : RecyclerViewHolder<DiscoveryDetail>(root) {

    private val view = ItemDiscoveryBinding.bind(root)

    override fun bind(item: DiscoveryDetail) {
      view.checkboxName.isChecked = item.discovered
      view.checkboxName.text = item.name
      view.textStar.text = item.star.toString()
      view.textTask.text = item.task
    }
  }

  private class Adapter : RecyclerListAdapter<DiscoveryDetail, Holder>(
    compareItem = { old, new -> old.compareWith(new) },
    compareContent = { old, new -> old == new },
    viewHolderCreator = { parent, _ -> Holder(parent.inflate(R.layout.item_discovery)) }
  ) {

    private var taskClick: (String) -> Unit = {}
    private var checkClick: () -> Unit = {}

    override fun onViewHolderCreated(holder: Holder) {
      super.onViewHolderCreated(holder)
      with(ItemDiscoveryBinding.bind(holder.itemView)) {
        textTask.onClick { taskClick(getItem(holder.adapterPosition).taskRef) }
        checkboxName.onClick {
          val index = holder.adapterPosition
          val isChecked = checkboxName.isChecked
          val updated = currentList[index].copy(discovered = isChecked)
          update(index, updated)
          checkClick()
        }
      }
    }

    override fun indexOf(item: DiscoveryDetail): Int = currentList.indexOfFirst { it.compareWith(item) }

    fun isAllChecked(): Boolean = currentList.find { !it.discovered } == null
    fun toggleCheckAll(isChecked: Boolean) = submitList(currentList.map { it.copy(discovered = isChecked) })
    fun update(index: Int, item: DiscoveryDetail) {
      val data = currentList.toMutableList()
      data[index] = item
      submitList(data)
    }

    fun onTaskClick(click: (String) -> Unit) = apply { taskClick = click }
    fun onCheckClick(click: () -> Unit) = apply { checkClick = click }
  }
}