package me.quenchjian.gvotool

import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

private val Context.caller: ActivityResultCaller
  get() = when (this) {
    is ActivityResultCaller -> this
    is ContextWrapper -> baseContext.caller
    else -> throw IllegalStateException()
  }

fun Context.openDocument(callback: ActivityResultCallback<Uri?>): ActivityResultLauncher<Array<String>> {
  return caller.registerForActivityResult(ActivityResultContracts.OpenDocument(), callback)
}