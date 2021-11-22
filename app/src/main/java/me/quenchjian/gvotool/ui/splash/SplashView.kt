package me.quenchjian.gvotool.ui.splash

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.zhuinden.simplestack.StateChange
import me.quenchjian.gvotool.R
import me.quenchjian.gvotool.databinding.ViewSplashBinding
import me.quenchjian.gvotool.ui.character.CharacterScreen
import me.quenchjian.gvotool.ui.dashboard.Dashboard
import me.quenchjian.gvotool.ui.events.onClick
import me.quenchjian.gvotool.ui.importdata.ImportScreen
import me.quenchjian.gvotool.ui.mvvm.ViewContainer
import me.quenchjian.gvotool.ui.navigation.Screen
import me.quenchjian.gvotool.ui.widgets.inflate
import me.quenchjian.gvotool.ui.widgets.show

class SplashView(
  root: View,
  vm: SplashViewModel,
  pageBinder: (Screen, View) -> Unit,
) : ViewContainer<SplashViewModel>(root, vm) {

  private val view = ViewSplashBinding.bind(root)
  private val pages = listOf(Tab.CHARACTER, Tab.DISCOVERY)

  init {
    view.pagerWizard.adapter = Adapter(pages, pageBinder)
    TabLayoutMediator(view.tabWizard, view.pagerWizard) { tab, position ->
      tab.text = context.getString(pages[position].nameRes)
    }.attach()
    view.buttonSkip.onClick { vm.skipInitialization() }
    view.buttonDone.onClick { backstack.replaceTop(Dashboard.StatisticsScreen, StateChange.REPLACE) }
  }

  init {
    vm.skip.observe(this) { skip ->
      if (skip) {
        backstack.replaceTop(Dashboard.StatisticsScreen, StateChange.REPLACE)
      } else {
        root.show()
      }
    }
  }

  private class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

  private class Adapter(
    private val pages: List<Tab>,
    private val onBind: (Screen, View) -> Unit,
  ) : RecyclerView.Adapter<Holder>() {

    override fun getItemCount(): Int = pages.size
    override fun getItemViewType(position: Int): Int = pages[position].layoutRes
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(parent.inflate(viewType))
    override fun onBindViewHolder(holder: Holder, position: Int) = onBind(pages[position].screen, holder.itemView)
  }

  private enum class Tab(val screen: Screen, val nameRes: Int, val layoutRes: Int) {
    CHARACTER(CharacterScreen, R.string.tab_character, R.layout.view_addcharacter),
    DISCOVERY(ImportScreen, R.string.tab_discovery, R.layout.view_importdiscovery),
  }
}