package me.quenchjian.gvotool.ui.importdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.quenchjian.gvotool.concurrent.WorkThread
import me.quenchjian.gvotool.ui.mvvm.ViewModel
import timber.log.Timber
import java.io.InputStream
import javax.inject.Inject

class ImportViewModel @Inject constructor(
  workThread: WorkThread,
  private val importDiscovery: ImportDiscovery,
) : ViewModel(workThread) {

  val importing: LiveData<Boolean> = MutableLiveData()
  val importError: LiveData<Throwable> = MutableLiveData()

  fun importExcel(inputStream: InputStream?) {
    Timber.tag("MVVM").d("import excel")
    inputStream ?: return
    launch {
      (importing as MutableLiveData).postValue(true)
      val result = withContext(workThread.io) { importDiscovery(inputStream) }
      importing.postValue(false)
      if (result.isFailure) {
        (importError as MutableLiveData).postValue(result.exceptionOrNull())
      }
    }
  }
}