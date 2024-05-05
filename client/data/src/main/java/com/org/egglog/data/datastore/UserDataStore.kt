package com.org.egglog.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.org.egglog.data.auth.model.UserResponse
import com.org.egglog.domain.auth.model.UserDetail
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "user_datastore")

class UserDataStore @Inject constructor(
    private val context: Context
) {

    companion object {
        private val USER_DETAIL = stringPreferencesKey("userDetail")
    }

    suspend fun setUser(user: UserDetail?) {
        val userJson = Json.encodeToString(user)
        context.dataStore.edit { preferences ->
            preferences[USER_DETAIL] = userJson
        }
    }

    suspend fun getUser(): UserDetail? {
        return context.dataStore.data.map { preferences ->
            preferences[USER_DETAIL]?.let { userJson ->
                Json.decodeFromString<UserDetail>(userJson)
            }
        }.firstOrNull()
    }


    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}