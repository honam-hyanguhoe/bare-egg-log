package com.org.egglog.presentation.domain.group.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.io.InputStream
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class FileUploadViewModel @Inject constructor(

) :ViewModel(), ContainerHost<FileUploadState, FileUploadSideEffect>{
    override val container: Container<FileUploadState, FileUploadSideEffect> = container(
        initialState = FileUploadState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(FileUploadSideEffect.Toast(message = throwable.message.orEmpty()))
                }
            }
        }
    )

//    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate : StateFlow<LocalDate?> = _selectedDate.asStateFlow()

    fun setSelectedDate(selected: LocalDate?) = intent{
        _selectedDate.value = selected
        Log.d("upload", "selected $selected")

        reduce {
            state.copy(
                uploadDutyDate = "${selected?.year}-${selected?.month}"
            )
        }
    }

    fun onChangeFileDay(value : String) = blockingIntent {
        reduce {
            val updateCustomDutyList = state.customDutyList.toMutableMap()
            updateCustomDutyList["DAY"] = value
            state.copy(
                customDutyList = updateCustomDutyList
            )
        }

        Log.d("upload", "customDutyList ${state.customDutyList["DAY"]}")
    }

    fun onChangeFileEVE(value : String) = blockingIntent {
        reduce {
            val updateCustomDutyList = state.customDutyList.toMutableMap()
            updateCustomDutyList["EVE"] = value
            state.copy(
                customDutyList = updateCustomDutyList
            )
        }
    }

    fun onChangeFileNIGHT(value : String) = blockingIntent {
        reduce {
            val updateCustomDutyList = state.customDutyList.toMutableMap()
            updateCustomDutyList["NIGHT"] = value
            state.copy(
                customDutyList = updateCustomDutyList
            )
        }
    }

    fun onChangeFileOFF(value : String) = blockingIntent {
        reduce {
            val updateCustomDutyList = state.customDutyList.toMutableMap()
            updateCustomDutyList["OFF"] = value
            state.copy(
                customDutyList = updateCustomDutyList
            )
        }
    }

    fun uploadFile(context: Context, uri: Uri) = blockingIntent {
        reduce { state.copy(isLoading = true, uploadSuccess = null, errorMessage = null) }

        try {
            val jsonResult = withContext(Dispatchers.IO) {
                val inputStream: InputStream = context.contentResolver.openInputStream(uri)
                    ?: throw Exception("Failed to open input stream from URI")

                FileUploadSideEffect.Toast("근무표 변환을 시작합니다.")

                val jsonData = inputStream.use { stream ->
                    val workbook = WorkbookFactory.create(stream)
                    val sheet = workbook.getSheetAt(0)

                    val data = mutableListOf<MutableList<String>>()

                    // Read data rows (from the first row onwards)
                    for (rowIndex in 1 until sheet.physicalNumberOfRows) {
                        val row = sheet.getRow(rowIndex)
                        val rowData = mutableListOf<String>()
                        for (cellIndex in 0 until row.physicalNumberOfCells) {
                            val cell = row.getCell(cellIndex)
                            val cellValue = cell?.toString() ?: ""
                            // Check if the value is a number and if it can be represented as an integer
                            val formattedCellValue = if (cellValue.toDoubleOrNull()?.rem(1) == 0.0) {
                                cellValue.toDouble().toInt().toString()
                            } else {
                                cellValue
                            }
                            rowData.add(formattedCellValue)
                        }
                        data.add(rowData)
                    }

                    workbook.close()
                    Gson().toJson(data)
                }

                jsonData
            }
            Log.d("upload", jsonResult)
            FileUploadSideEffect.Toast("근무표를 업로드했습니다.")

            reduce {
                state.copy(
                    isLoading = false,
                    uploadSuccess = true,
                    dutyJsonData = jsonResult

                ) }
        } catch (e: Exception) {
            Log.e("upload", "Error uploading file", e)
            FileUploadSideEffect.Toast("근무표 업로드를 실패했습니다.")
            reduce { state.copy(isLoading = false, uploadSuccess = false, errorMessage = e.message) }
        }
    }

}

data class FileUploadState(
    val uploadDutyDate : String = "${LocalDate.now().year}-${LocalDate.now().month}",
    val customDutyList: Map<String, String> = mapOf<String, String>(
        "DAY" to "",
        "EVE" to "",
        "NIGHT" to "",
        "OFF" to ""
    ),
    val dutyJsonData: String = "",
    val isLoading: Boolean = false,
    val uploadSuccess: Boolean? = null,
    val errorMessage: String? = null
)
sealed interface FileUploadSideEffect{
    class Toast(val message: String) : FileUploadSideEffect
}