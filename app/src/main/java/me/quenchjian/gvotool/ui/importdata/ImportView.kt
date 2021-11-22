package me.quenchjian.gvotool.ui.importdata

import android.net.Uri
import android.view.View
import me.quenchjian.gvotool.R
import me.quenchjian.gvotool.databinding.ViewImportdiscoveryBinding
import me.quenchjian.gvotool.openDocument
import me.quenchjian.gvotool.ui.events.onClick
import me.quenchjian.gvotool.ui.mvvm.State
import me.quenchjian.gvotool.ui.mvvm.ViewContainer
import me.quenchjian.gvotool.ui.widgets.LoadingDialog
import timber.log.Timber

class ImportView(root: View, vm: ImportViewModel) : ViewContainer<ImportViewModel>(root, vm) {

  private val view = ViewImportdiscoveryBinding.bind(root)
  private val openDocument = context.openDocument(this::documentOpened)
  private var loadingDialog: LoadingDialog? = null

  init {
    Timber.tag("MVVM").d("init ViewModel observer")
    vm.importState.observe(this) { state ->
      Timber.d("state change to: $state")
      when (state) {
        State.Busy -> toggleImporting(true)
        State.Idle -> toggleImporting(false)
        is State.Success -> showImportResult("Import Success")
        is State.Error -> showImportResult(state.t.message ?: "Unknown Error")
      }
    }
    view.buttonImport.onClick { openDocument.launch(arrayOf("application/*")) }
  }

  private fun toggleImporting(active: Boolean) {
    val dialog = loadingDialog ?: LoadingDialog(context).apply { loadingDialog = this }
    dialog.loadingMessage(R.string.busy_importing)
    if (active) dialog.show() else dialog.dismiss()
  }

  private fun showImportResult(msg: String) {
    view.textImportResult.text = msg
  }

  private fun documentOpened(uri: Uri?) {
    uri ?: return
    vm.importExcel(context.contentResolver.openInputStream(uri))
  }
}