package com.org.egglog.presentation.domain.group.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.group.model.DutyTag
import com.org.egglog.domain.group.model.FormattedFile
import com.org.egglog.domain.group.model.UploadDutyFile
import com.org.egglog.domain.group.usecase.GetTagUseCase
import com.org.egglog.domain.group.usecase.UploadDutyFileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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
    private val getUserTokenUseCase: GetTokenUseCase,
    private val getUserStoreUseCase: GetUserStoreUseCase,
    private val getTagUseCase: GetTagUseCase,
    private val uploadDutyFileUseCase: UploadDutyFileUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<FileUploadState, FileUploadSideEffect> {
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
    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()
    val groupId = savedStateHandle.get<Long>("groupId")
        ?: throw IllegalStateException("GroupId must be provided")

    init {
        initDutyTag()
    }

    fun initDutyTag() = intent {
        val tokens = getUserTokenUseCase()

        val result = getTagUseCase(
            accessToken = "Bearer ${tokens.first}",
            groupId = groupId
        ).getOrNull()

        Log.d("upload", "init ${result}")
        reduce {
            val updateCustomDutyList = state.customDutyList.toMutableMap()
            updateCustomDutyList["DAY"] = result?.day ?: ""
            updateCustomDutyList["EVE"] = result?.eve ?: ""
            updateCustomDutyList["NIGHT"] = result?.night ?: ""
            updateCustomDutyList["OFF"] = result?.off ?: ""

            state.copy(
                customDutyList = updateCustomDutyList
            )
        }
    }

    fun setSelectedDate(selected: LocalDate?) = intent {
        _selectedDate.value = selected
        Log.d("upload", "selected $selected")

        if(selected?.monthValue!! < 10){
            reduce {
                state.copy(
                    uploadDutyDate = "${selected?.year}-0${selected?.monthValue}"
                )
            }
        }else{
            reduce {
                state.copy(
                    uploadDutyDate = "${selected?.year}-${selected?.monthValue}"
                )
            }
        }
    }

    fun onChangeFileDay(value: String) = blockingIntent {
        reduce {
            val updateCustomDutyList = state.customDutyList.toMutableMap()
            updateCustomDutyList["DAY"] = value
            state.copy(
                customDutyList = updateCustomDutyList
            )
        }

        Log.d("upload", "customDutyList ${state.customDutyList["DAY"]}")
    }

    fun onChangeFileEVE(value: String) = blockingIntent {
        reduce {
            val updateCustomDutyList = state.customDutyList.toMutableMap()
            updateCustomDutyList["EVE"] = value
            state.copy(
                customDutyList = updateCustomDutyList
            )
        }
    }

    fun onChangeFileNIGHT(value: String) = blockingIntent {
        reduce {
            val updateCustomDutyList = state.customDutyList.toMutableMap()
            updateCustomDutyList["NIGHT"] = value
            state.copy(
                customDutyList = updateCustomDutyList
            )
        }
    }

    fun onChangeFileOFF(value: String) = blockingIntent {
        reduce {
            val updateCustomDutyList = state.customDutyList.toMutableMap()
            updateCustomDutyList["OFF"] = value
            state.copy(
                customDutyList = updateCustomDutyList
            )
        }
    }

    fun uploadFile(context: Context, uri: Uri, groupName : String) = blockingIntent {
        Log.d("upload", groupName)
        val tokens = getUserTokenUseCase()

        formatFileToJson(context, uri)

        val result = uploadDutyFileUseCase(
            accessToken = "Bearer ${tokens.first}",
            groupId = groupId,
            groupName = groupName,
            dutyFileData = UploadDutyFile(
                date = state.uploadDutyDate,
                dutyList = state.dutyJsonData,
                customWorkTag = DutyTag(
                    day = state.customDutyList["DAY"]!!,
                    eve = state.customDutyList["EVE"]!!,
                    night = state.customDutyList["NIGHT"]!!,
                    off = state.customDutyList["OFF"]!!,
                ),
                day = LocalDate.now().toString()
            )
        )


        if (result.isSuccess) {
            Log.d("upload", result.getOrNull().toString())
        }
    }

    fun formatFileToJson(context: Context, uri: Uri) = blockingIntent {
        reduce { state.copy(isLoading = true, uploadSuccess = null, errorMessage = null) }
        val dataToFormattedFile: List<FormattedFile>

        try {
            val jsonResult = withContext(Dispatchers.IO) {
                val inputStream: InputStream = context.contentResolver.openInputStream(uri)
                    ?: throw Exception("Failed to open input stream from URI")

                FileUploadSideEffect.Toast("근무표 변환을 시작합니다.")

                val jsonData = inputStream.use { stream ->
                    val workbook = WorkbookFactory.create(stream)
                    val sheet = workbook.getSheetAt(0)

                    // day 값
                    val dayRow = sheet.getRow(0)
                    val days = mutableListOf<String>()
                    for (cellIndex in 2 until dayRow.physicalNumberOfCells) {
                        val cell = dayRow.getCell(cellIndex)
                        val cellValue = cell?.toString() ?: ""
                        val formattedCellValue =
                            if (cellValue.toDoubleOrNull()?.rem(1) == 0.0) {
                                cellValue.toDouble().toInt().toString()
                            } else {
                                cellValue
                            }
                        days.add(formattedCellValue)
                    }


                    val data = mutableListOf<Map<String, Any>>()

                    for (rowIndex in 1 until sheet.physicalNumberOfRows) {
                        val row = sheet.getRow(rowIndex)
                        if (row != null) {
                            val employeeData = mutableMapOf<String, Any>()
                            val workData = mutableMapOf<String, String>()

                            // 사번
                            val employeeId = row.getCell(0)?.let { cell ->
                                val cellValue = cell.toString()
                                if (cellValue.toDoubleOrNull()?.rem(1) == 0.0) {
                                    cellValue.toDouble().toInt().toString()
                                } else {
                                    cellValue
                                }
                            } ?: ""
                            employeeData["employeeId"] = employeeId

                            // 이름
                            val employeeName = row.getCell(1)?.toString() ?: ""
                            employeeData["name"] = employeeName

                            // 근무 정보
                            for (cellIndex in 2 until row.physicalNumberOfCells) {
                                val cell = row.getCell(cellIndex)
                                val cellValue = cell?.toString() ?: ""
                                val formattedCellValue =
                                    if (cellValue.toDoubleOrNull()?.rem(1) == 0.0) {
                                        cellValue.toDouble().toInt().toString()
                                    } else {
                                        cellValue
                                    }
                                workData[days[cellIndex - 2]] = formattedCellValue
                            }

                            employeeData["work"] = workData
                            data.add(employeeData)
                        }
                    }

                    workbook.close()
                    Gson().toJson(data)
                }
                jsonData

                val json = Json { ignoreUnknownKeys = true }
                dataToFormattedFile = json.decodeFromString<List<FormattedFile>>(jsonData)
            }
            Log.d("upload", dataToFormattedFile.toString())




            FileUploadSideEffect.Toast("근무표를 업로드했습니다.")

            reduce {
                state.copy(
                    isLoading = false,
                    uploadSuccess = true,
                    dutyJsonData = dataToFormattedFile

                )
            }
        } catch (e: Exception) {
            Log.e("upload", "Error uploading file", e)
            FileUploadSideEffect.Toast("근무표 업로드를 실패했습니다.")
            reduce {
                state.copy(
                    isLoading = false,
                    uploadSuccess = false,
                    errorMessage = e.message
                )
            }
        }
    }
}

data class FileUploadState(
    val uploadDutyDate: String = "${LocalDate.now().year}-${LocalDate.now().monthValue}",
    val customDutyList: Map<String, String> = mapOf<String, String>(
        "DAY" to "",
        "EVE" to "",
        "NIGHT" to "",
        "OFF" to ""
    ),
    val dutyJsonData: List<FormattedFile> = emptyList(),
    val isLoading: Boolean = false,
    val uploadSuccess: Boolean? = null,
    val errorMessage: String? = null
)

sealed interface FileUploadSideEffect {
    class Toast(val message: String) : FileUploadSideEffect
}