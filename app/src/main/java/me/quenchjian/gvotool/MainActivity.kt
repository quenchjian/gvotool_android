package me.quenchjian.gvotool

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.zhuinden.simplestack.navigator.Navigator
import me.quenchjian.gvotool.navigation.NavKey

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    Navigator.install(this, findViewById(R.id.activity_container), intent.initStack)
    onBackPressedDispatcher.addCallback(BackPressHandler(this))
  }

  private class BackPressHandler(private val activity: MainActivity) : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
      if (!Navigator.onBackPressed(activity)) {
        isEnabled = false
        activity.finishAfterTransition()
      }
    }
  }

  companion object {
    private var Intent.initStack: List<NavKey>
      get() = getParcelableArrayListExtra("intent-extra-init-stack") ?: emptyList()
      set(value) {
        putParcelableArrayListExtra("intent-extra-init-stack", ArrayList(value))
      }

    fun intent(context: Context, vararg stack: NavKey): Intent {
      return Intent.makeMainActivity(ComponentName(context, MainActivity::class.java)).apply {
        initStack = stack.asList()
      }
    }
  }
}