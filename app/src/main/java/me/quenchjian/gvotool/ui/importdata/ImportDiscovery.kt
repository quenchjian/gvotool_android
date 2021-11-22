package me.quenchjian.gvotool.ui.importdata

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.withContext
import me.quenchjian.gvotool.concurrent.Dispatcher
import me.quenchjian.gvotool.data.GvoDatabase
import me.quenchjian.gvotool.data.Settings
import me.quenchjian.gvotool.data.entity.Discovery
import me.quenchjian.gvotool.ui.mvvm.ObservableUseCase
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.util.CellReference
import timber.log.Timber
import java.io.InputStream
import javax.inject.Inject

class ImportDiscovery @Inject constructor(
  dispatcher: Dispatcher,
  private val db: GvoDatabase,
  private val settings: Settings,
) : ObservableUseCase<Unit>(dispatcher) {

  @MainThread
  operator fun invoke(input: InputStream) = submit { execute(input) }

  @Suppress("BlockingMethodInNonBlockingContext")
  @WorkerThread
  suspend fun execute(input: InputStream) = withContext(dispatcher.io) {
    input.use { inputStream ->
      val workbook = WorkbookFactory.create(inputStream)
      val discoveries = mutableListOf<Discovery>()
      for (i in 0 until workbook.numberOfSheets) {
        discoveries.addAll(parseSheet(workbook.getSheetAt(i)))
      }
      db.discoveryDao().clear()
      db.discoveryDao().init(discoveries)
      db.characterDao().queryAll().forEach {
        db.discoveryDao().initState(it.id)
      }
      settings.discoveryImported = true
    }
  }

  private fun parseSheet(sheet: Sheet): List<Discovery> {
    Timber.d("parse ${sheet.sheetName} sheet")
    val discoverySheet = DiscoverySheet.fromName(sheet.sheetName) ?: return emptyList()
    val discoveryType = discoverySheet.type ?: return emptyList()
    val columns = discoverySheet.columns.map { CellReference.convertColStringToIndex(it) }
    val result = mutableListOf<Discovery>()
    for (i in 1 until sheet.physicalNumberOfRows) {
      val row = sheet.getRow(i)
      val nameCell = row.getCell(columns[1])
      if (nameCell.cellType == CellType.STRING) {
        val discovery = Discovery(
          type = discoveryType,
          name = stringFormatter.formatCellValue(nameCell),
          task = stringFormatter.formatCellValue(row.getCell(columns[2])),
          taskRef = row.getCell(columns[2]).hyperlink?.address ?: "",
          star = stringFormatter.formatCellValue(row.getCell(columns[3])).toStar(),
          card = row.getCell(columns[4]).numericCellValue.toInt(),
          merit = row.getCell(columns[5]).numericCellValue.toInt(),
          version = stringFormatter.formatCellValue(row.getCell(columns[6]))
        )
        result.add(discovery)
      }
      if (nameCell.cellType == CellType.FORMULA) {
        break
      }
    }
    Timber.d("parse ${sheet.sheetName} sheet completed, size = ${result.size}")
    return result
  }

  companion object {
    private val stringFormatter = DataFormatter()

    private fun String.toStar(): Int {
      return when (this) {
        "一" -> 1
        "二" -> 2
        "三" -> 3
        "四" -> 4
        "五" -> 5
        else -> 0
      }
    }
  }
}