package me.quenchjian.gvotool.ui.importdata

import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.android.material.snackbar.Snackbar
import me.quenchjian.gvotool.databinding.ViewImportexcelBinding
import me.quenchjian.gvotool.openDocument
import me.quenchjian.gvotool.ui.events.onClick
import me.quenchjian.gvotool.ui.mvvm.ViewContainer
import timber.log.Timber

class ImportView(root: View, vm: ImportViewModel) : ViewContainer(root) {

  private val view = ViewImportexcelBinding.bind(root)
  private val openDocument = context.openDocument { vm.importExcel(it) }
  private var loadingDialog: Dialog? = null

  init {
    Timber.tag("MVVM").d("bindViewModel")
    vm.importing.observe(this) { toggleImporting(it) }
    vm.importError.observe(this) { showImportFail(it.message ?: "Unknown Error") }
    view.buttonImport.onClick { openDocument.launch(emptyArray()) }
  }

  private fun toggleImporting(active: Boolean) {
    val dialog = loadingDialog ?: Dialog(context).apply {
      loadingDialog = this
      setContentView(ProgressBar(context), ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
      ))
    }
    if (active) dialog.show() else dialog.dismiss()
  }

  private fun showImportFail(msg: String) {
    Snackbar.make(root, msg, Snackbar.LENGTH_SHORT).show()
  }
}