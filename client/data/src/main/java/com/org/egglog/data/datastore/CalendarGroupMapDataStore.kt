package com.org.egglog.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// DataStore 인스턴스를 확장 속성을 통해 생성
private val Context.dataStore by preferencesDataStore(name = "calendar_group_map_datastore")

class CalendarGroupMapDataStore @Inject constructor(
    private val context: Context
) {
    companion object {
        private val CALENDAR_GROUP_LIST = stringPreferencesKey("calendarGroupMap")
    }

    private val gson = Gson()

    // Map<String, Boolean>를 JSON으로 직렬화하여 저장
    suspend fun setMap(calendarGroupPair: Map<String, Boolean>) {
        val json = gson.toJson(calendarGroupPair)
        context.dataStore.edit { preferences ->
            preferences[CALENDAR_GROUP_LIST] = json
        }
    }

    // 저장된 JSON을 Map<String, Boolean>로 역직렬화하여 반환
    suspend fun getMap(): Map<String, Boolean> {
        val type = object : TypeToken<Map<String, Boolean>>() {}.type
        return context.dataStore.data.map { preferences ->
            preferences[CALENDAR_GROUP_LIST]?.let { json ->
                gson.fromJson<Map<String, Boolean>>(json, type)
            } ?: emptyMap()
        }.first()
    }

    // 모든 저장된 값 삭제
    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
