package me.quenchjian.gvotool.ui.importdata

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.quenchjian.gvotool.ui.mvvm.ViewModel
import timber.log.Timber
import javax.inject.Inject

class ImportViewModel @Inject constructor() : ViewModel(), CoroutineScope by MainScope() {

  val importing: LiveData<Boolean> = MutableLiveData()
  val importError: LiveData<Throwable> = MutableLiveData()

  fun importExcel(uri: Uri?) {
    Timber.tag("MVVM").d("import excel")
    uri ?: return
    launch {
      (importing as MutableLiveData).postValue(true)
      withContext(Dispatchers.IO) { delay(1000) }
      importing.postValue(false)
      (importError as MutableLiveData).postValue(NotImplementedError("Not Implemented!!"))
    }
  }
}