package me.quenchjian.gvotool.ui.discovery

import android.content.Context
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textview.MaterialTextView
import me.quenchjian.gvotool.R
import me.quenchjian.gvotool.data.entity.Discovery
import me.quenchjian.gvotool.databinding.ViewDiscoveriesBinding
import me.quenchjian.gvotool.ui.dashboard.DashboardView
import me.quenchjian.gvotool.ui.dashboard.DashboardViewModel
import me.quenchjian.gvotool.ui.navigation.Screen
import me.quenchjian.gvotool.ui.widgets.inflate

class DiscoveriesView(
  root: View,
  vm: DashboardViewModel,
  pageBinder: (Screen, View) -> Unit,
) : DashboardView<DashboardViewModel>(root, R.layout.view_discoveries, vm) {

  private val view = ViewDiscoveriesBinding.bind(container)
  private val names = context.resources.getStringArray(R.array.discovery_types)
  private val drawables = IntArray(names.size)

  init {
    context.resources.obtainTypedArray(R.array.discovery_drawable).run {
      for (index in drawables.indices) {
        drawables[index] = getResourceId(index, 0)
      }
      recycle()
    }
  }

  init {
    view.pagerDiscoveries.adapter = Adapter(Discovery.Type.values(), pageBinder)
    val iconSize = 24.dp(context)
    val iconPadding = 4.dp(context)
    TabLayoutMediator(view.tabDiscoveries, view.pagerDiscoveries) { tab, position ->
      tab.customView = MaterialTextView(context).apply {
        val icon = ContextCompat.getDrawable(context, drawables[position])
        icon?.bounds = Rect(0, 0, iconSize, iconSize)
        setCompoundDrawables(null, icon, null, null)
        compoundDrawablePadding = iconPadding
        setTextColor(view.tabDiscoveries.tabTextColors)
        text = names[position]
        gravity = Gravity.CENTER
      }
    }.attach()
  }

  private class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

  private class Adapter(
    private val pages: Array<Discovery.Type>,
    private val onBind: (Screen, View) -> Unit,
  ) : RecyclerView.Adapter<Holder>() {

    override fun getItemCount(): Int = pages.size
    override fun getItemViewType(position: Int): Int = R.layout.view_discovery
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(viewType))
    override fun onBindViewHolder(holder: Holder, position: Int) {
      onBind(DiscoveryScreen(pages[position]), holder.itemView)
    }
  }

  companion object {
    private fun Int.dp(context: Context): Int {
      return (this * context.resources.displayMetrics.density).toInt()
    }
  }
}