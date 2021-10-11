package me.quenchjian.gvotool.ui.mvvm

import kotlinx.coroutines.CoroutineScope
import me.quenchjian.gvotool.concurrent.WorkThread
import kotlin.coroutines.CoroutineContext

/**
 * ViewModel for MVVM architecture
 */
abstract class ViewModel(val workThread: WorkThread) : CoroutineScope {

  override val coroutineContext: CoroutineContext = workThread.ui
}