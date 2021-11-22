package me.quenchjian.gvotool.ui.widgets

import android.app.Dialog
import android.content.Context
import androidx.annotation.StringRes
import com.google.android.material.textview.MaterialTextView
import me.quenchjian.gvotool.R

class LoadingDialog(context: Context) : Dialog(context, R.style.AppTheme_Dialog) {

  private var message: String = ""

  init {
    setContentView(R.layout.dialog_loading)
  }

  override fun onStart() {
    super.onStart()
    findViewById<MaterialTextView>(R.id.text_loading).text = message
  }

  fun loadingMessage(@StringRes stringRes: Int, vararg arg: Any) =
    apply { message = context.getString(stringRes, *arg) }

  fun loadingMessage(msg: String) = apply { message = msg }
}