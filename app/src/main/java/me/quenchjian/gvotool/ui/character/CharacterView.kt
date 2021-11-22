package me.quenchjian.gvotool.ui.character

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import me.quenchjian.gvotool.R
import me.quenchjian.gvotool.data.entity.Character
import me.quenchjian.gvotool.databinding.FormCharacterBinding
import me.quenchjian.gvotool.databinding.ItemCharacterBinding
import me.quenchjian.gvotool.databinding.ViewAddcharacterBinding
import me.quenchjian.gvotool.ui.events.onClick
import me.quenchjian.gvotool.ui.mvvm.State
import me.quenchjian.gvotool.ui.mvvm.ViewContainer
import me.quenchjian.gvotool.ui.widgets.LoadingDialog
import me.quenchjian.gvotool.ui.widgets.RecyclerListAdapter
import me.quenchjian.gvotool.ui.widgets.RecyclerViewHolder
import me.quenchjian.gvotool.ui.widgets.hide
import me.quenchjian.gvotool.ui.widgets.hideIf
import me.quenchjian.gvotool.ui.widgets.inflate
import me.quenchjian.gvotool.ui.widgets.show
import me.quenchjian.gvotool.ui.widgets.showIf

class CharacterView(root: View, vm: CharacterViewModel) : ViewContainer<CharacterViewModel>(root, vm) {

  private val view = ViewAddcharacterBinding.bind(root)
  private val adapter: Adapter get() = view.recyclerCharacter.adapter as Adapter
  private var loadingDialog: LoadingDialog? = null
  private var formDialog: BottomSheetDialog? = null

  init {
    view.recyclerCharacter.layoutManager = LinearLayoutManager(context)
    view.recyclerCharacter.adapter = Adapter().onEditClick { showCharacterForm(it) }
    view.fabAdd.onClick { showCharacterForm(null) }
  }

  init {
    vm.loadState.observe(this) { state ->
      if (state is State.Success) {
        val isEmpty = state.value.isEmpty()
        view.recyclerCharacter.hideIf { isEmpty }
        view.textEmpty.showIf { isEmpty }
        adapter.submitList(state.value)
      }
    }
    vm.addEditState.observe(this) { state ->
      when (state) {
        State.Busy -> toggleAdding(true)
        State.Idle -> toggleAdding(false)
        is State.Success -> {
          view.recyclerCharacter.show()
          view.textEmpty.hide()
          when (val id = adapter.indexOf(state.value)) {
            -1 -> adapter.add(state.value)
            else -> adapter.set(id, state.value)
          }
        }
        is State.Error -> showError(state.t)
      }
    }
    vm.loadCharacters()
  }

  private fun toggleAdding(active: Boolean) {
    val dialog = loadingDialog ?: LoadingDialog(context).also { loadingDialog = it }
    dialog.loadingMessage(R.string.busy_adding)
    if (active) dialog.show() else dialog.dismiss()
  }

  private fun showCharacterForm(character: Character?) {
    val dialog = formDialog ?: BottomSheetDialog(context).also { formDialog = it }
    val formView = FormCharacterBinding.inflate(LayoutInflater.from(context)).apply {
      if (character != null) {
        editCharacterAccount.setText(character.account)
        spinnerCharacterServer.setSelection(context.resources.getStringArray(R.array.servers).indexOf(character.server))
        editCharacterName.setText(character.name)
      }
      buttonSave.onClick {
        val form = CharacterForm(
          account = editCharacterAccount.text.toString(),
          server = spinnerCharacterServer.selectedItem.toString(),
          name = editCharacterName.text.toString()
        )
        vm.addCharacter(form)
        dialog.dismiss()
      }
    }
    dialog.setContentView(formView.root)
    if (dialog.isShowing) dialog.dismiss() else dialog.show()
  }

  private fun showError(t: Throwable) {
    val msg = when (t) {
      AccountEmpty -> context.getString(R.string.error_account_empty)
      ServerEmpty -> context.getString(R.string.error_server_empty)
      NameEmpty -> context.getString(R.string.error_name_empty)
      CharacterExisted -> context.getString(R.string.error_character_exist)
      else -> t.message ?: context.getString(R.string.error_unknown)
    }
    Snackbar.make(root, msg, Snackbar.LENGTH_SHORT).show()
  }

  private class Holder(itemView: View) : RecyclerViewHolder<Character>(itemView) {
    val view = ItemCharacterBinding.bind(itemView)

    override fun bind(item: Character) {
      view.textCharacterAccount.text = item.account
      view.textCharacterServer.text = item.server
      view.textCharacterName.text = item.name
    }
  }

  private class Adapter : RecyclerListAdapter<Character, Holder>(
    compareItem = { old, new -> old.id == new.id },
    compareContent = { old, new -> old == new },
    viewHolderCreator = { parent, _ -> Holder(parent.inflate(R.layout.item_character)) },
  ) {
    private var editClick: (Character) -> Unit = {}

    override fun onViewHolderCreated(holder: Holder) {
      super.onViewHolderCreated(holder)
      holder.view.buttonEdit.onClick { editClick(getItem(holder.adapterPosition)) }
    }

    override fun indexOf(item: Character): Int {
      return currentList.indexOfFirst { it.id == item.id }
    }

    fun onEditClick(click: (Character) -> Unit) = apply { editClick = click }
  }
}