package me.quenchjian.gvotool

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.zhuinden.simplestack.navigator.Navigator

fun Context.openDocument(callback: ActivityResultCallback<Uri?>): ActivityResultLauncher<Array<String>> {
  val frag = ActivityResultFragment.get(this)
  frag.callbacks.add(callback)
  return frag.openDocument
}

fun Context.openBrowser(url: String) {
  if (url.isNotEmpty()) {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
  }
}

class ActivityResultFragment : Fragment() {

  val callbacks = mutableSetOf<ActivityResultCallback<Uri?>>()
  val openDocument = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
    callbacks.forEach { callback -> callback.onActivityResult(uri) }
  }

  companion object {
    private const val TAG = "tag-activity-result"
    fun init(activity: FragmentActivity) {
      var frag = activity.find()
      if (frag == null) {
        frag = ActivityResultFragment()
        activity.supportFragmentManager.beginTransaction().add(frag, TAG).commit()
        activity.supportFragmentManager.executePendingTransactions()
      }
    }

    fun get(context: Context): ActivityResultFragment {
      return context.find()
        ?: throw IllegalStateException("ActivityResultFragment didn't init yet")
    }

    private fun Context.find(): ActivityResultFragment? {
      return Navigator.findActivity<FragmentActivity>(this).supportFragmentManager
        .findFragmentByTag(TAG) as? ActivityResultFragment
    }
  }
}