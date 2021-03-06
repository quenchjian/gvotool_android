package me.quenchjian.gvotool

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.zhuinden.simplestack.navigator.DefaultStateChanger
import com.zhuinden.simplestack.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import me.quenchjian.gvotool.ui.mvvm.MvvmBinder
import me.quenchjian.gvotool.ui.mvvm.ViewModelFactory
import me.quenchjian.gvotool.ui.navigation.Screen
import me.quenchjian.gvotool.ui.splash.SplashScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  @Inject lateinit var vmFactory: ViewModelFactory

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val container: ViewGroup = findViewById(R.id.activity_container)
    Navigator
      .configure()
      .setStateChanger(DefaultStateChanger.configure()
        .setViewChangeCompletionListener(MvvmBinder(savedStateRegistry, vmFactory))
        .create(this, container))
      .install(this, container, intent.initStack)
    ActivityResultFragment.init(this)
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
    private var Intent.initStack: List<Screen>
      get() = getParcelableArrayListExtra("intent-extra-init-stack") ?: listOf(SplashScreen)
      set(value) {
        putParcelableArrayListExtra("intent-extra-init-stack", ArrayList(value))
      }

    fun intent(context: Context, vararg stack: Screen): Intent {
      return Intent.makeMainActivity(ComponentName(context, MainActivity::class.java)).apply {
        initStack = stack.asList()
      }
    }
  }
}