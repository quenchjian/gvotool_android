package me.quenchjian.gvotool.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zhuinden.simplestack.StateChange
import me.quenchjian.gvotool.R
import me.quenchjian.gvotool.data.entity.Character
import me.quenchjian.gvotool.databinding.DialogCharacterChooserBinding
import me.quenchjian.gvotool.databinding.ItemCharacterBinding
import me.quenchjian.gvotool.databinding.ViewDashboardBinding
import me.quenchjian.gvotool.ui.mvvm.ViewContainer
import me.quenchjian.gvotool.ui.widgets.RecyclerListAdapter
import me.quenchjian.gvotool.ui.widgets.RecyclerViewHolder
import me.quenchjian.gvotool.ui.widgets.hide
import me.quenchjian.gvotool.ui.widgets.inflate

abstract class DashboardView<VM : DashboardViewModel>(
  root: View,
  @LayoutRes private val layoutRes: Int,
  vm: VM,
) : ViewContainer<VM>(root, vm) {

  private val view = ViewDashboardBinding.bind(root).apply { dashboardContainer.layoutResource = layoutRes }
  protected val container: View = view.dashboardContainer.inflate()

  init {
    view.toolbarCharacter.setTitleTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
    view.toolbarCharacter.setOnMenuItemClickListener {
      CharacterDialog(context, vm.characters)
        .onCharacterClick { vm.selectCharacter(it) }
        .show()
      true
    }
    view.dashboardNavigation.selectedItemId = when (layoutRes) {
      R.layout.view_statictics -> R.id.action_statistics
      R.layout.view_discoveries -> R.id.action_discovery
      R.layout.view_management -> R.id.action_management
      else -> -1
    }
    view.dashboardNavigation.setOnItemSelectedListener { item ->
      val (id, screen) = when (item.itemId) {
        R.id.action_statistics -> R.layout.view_statictics to Dashboard.StatisticsScreen
        R.id.action_discovery -> R.layout.view_discoveries to Dashboard.DiscoveriesScreen
        R.id.action_management -> R.layout.view_management to Dashboard.ManagementScreen
        else -> return@setOnItemSelectedListener false
      }
      if (id == layoutRes) {
        false
      } else {
        backstack.replaceTop(screen, StateChange.REPLACE)
        true
      }
    }
  }

  @CallSuper
  override fun attach(savedState: Bundle) {
    vm.character.observe(this) { view.toolbarCharacter.title = it.name }
  }

  private class Holder(root: View) : RecyclerViewHolder<Character>(root) {

    private val view = ItemCharacterBinding.bind(root)

    override fun bind(item: Character) {
      view.textCharacterAccount.text = item.account
      view.textCharacterServer.text = item.server
      view.textCharacterName.text = item.name
      view.buttonEdit.hide()
    }
  }

  private class Adapter : RecyclerListAdapter<Character, Holder>(
    compareItem = { old, new -> old.id == new.id },
    compareContent = { old, new -> old == new },
    viewHolderCreator = { parent, _ -> Holder(parent.inflate(R.layout.item_character)) }
  )

  private class CharacterDialog(
    context: Context,
    private val characters: List<Character>,
  ) : BottomSheetDialog(context) {

    private val view: DialogCharacterChooserBinding
    private var characterClick: (Character) -> Unit = {}

    init {
      setContentView(R.layout.dialog_character_chooser)
      view = DialogCharacterChooserBinding.bind(findViewById(R.id.dialog_character_chooser)!!)
    }

    override fun onStart() {
      super.onStart()
      val adapter = Adapter().onItemClick {
        dismiss()
        characterClick(it)
      }
      view.recyclerCharacterChooser.adapter = adapter
      view.recyclerCharacterChooser.layoutManager = LinearLayoutManager(context)
      adapter.submitList(characters)
    }

    fun onCharacterClick(click: (Character) -> Unit) = apply { characterClick = click }
  }
}