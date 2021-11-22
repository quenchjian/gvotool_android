package me.quenchjian.gvotool.ui.statistics

import android.view.View
import com.google.android.material.snackbar.Snackbar
import me.quenchjian.gvotool.R
import me.quenchjian.gvotool.data.entity.DiscoveryStatistics
import me.quenchjian.gvotool.databinding.ItemDiscoveryCountBinding
import me.quenchjian.gvotool.databinding.ViewStaticticsBinding
import me.quenchjian.gvotool.ui.dashboard.DashboardView
import me.quenchjian.gvotool.ui.mvvm.State
import me.quenchjian.gvotool.ui.widgets.inflate

class StatisticsView(
  root: View,
  vm: StatisticsViewModel,
) : DashboardView<StatisticsViewModel>(root, R.layout.view_statictics, vm) {

  private val view = ViewStaticticsBinding.bind(container)

  init {
    vm.medalRank.observe(this) { showMedalAndRank(it) }
    vm.discoveryState.observe(this) { state ->
      when (state) {
        is State.Success -> showDiscoveryStatistics(state.value)
        is State.Error -> showError(state.t.message)
        else -> Unit
      }
    }
  }

  private fun showMedalAndRank(value: MedalRank) {
    view.textMedal.text = context.getString(R.string.text_medal, value.medal)
    view.textRank.text = context.getString(R.string.text_rank_adventure, value.rank)
  }

  private fun showDiscoveryStatistics(statistics: List<DiscoveryStatistics>) {
    statistics.forEach { data ->
      val holder = Holder(view.layoutDiscoveries.inflate(R.layout.item_discovery_count))
      holder.bind(data)
      view.layoutDiscoveries.addView(holder.root)
    }
  }

  private fun showError(message: String?) {
    val msg = if (message.isNullOrEmpty()) context.getString(R.string.error_unknown) else message
    Snackbar.make(root, msg, Snackbar.LENGTH_SHORT).show()
  }

  private class Holder(val root: View) {

    private val view = ItemDiscoveryCountBinding.bind(root)
    private val context = root.context
    private val names = context.resources.getStringArray(R.array.discovery_types)
    private val drawables = IntArray(names.size)

    init {
      context.resources.obtainTypedArray(R.array.discovery_drawable).run {
        for (i in drawables.indices) {
          drawables[i] = getResourceId(i, 0)
        }
        recycle()
      }
    }

    fun bind(data: DiscoveryStatistics) {
      view.imageType.setImageResource(drawables[data.type.id])
      view.textType.text = names[data.type.id]
      view.textNumberTotal.text = data.merit.toString()
      view.textNumberCurrent.text = data.discovered.toString()
    }
  }
}