package me.quenchjian.gvotool.concurrent

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class Dispatcher @Inject constructor() {

  private val executor = Executors.newCachedThreadPool() as ThreadPoolExecutor

  init {
    executor.corePoolSize = 3
  }

  val io: CoroutineContext = executor.asCoroutineDispatcher()
  val main: CoroutineContext = SupervisorJob() + Dispatchers.Main
}