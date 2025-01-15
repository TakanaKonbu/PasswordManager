package com.takanakonbu.passwordmanager.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(context: Context) {
    private val dataStore = context.settingsDataStore

    companion object {
        private val APP_LOCK_ENABLED = booleanPreferencesKey("app_lock_enabled")
        private val APP_LOCK_PIN = stringPreferencesKey("app_lock_pin")
    }

    // 常にデータが存在するように初期値を設定
    val appLockEnabled: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[APP_LOCK_ENABLED] ?: false
        }

    val appLockPin: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[APP_LOCK_PIN]
        }

    suspend fun setAppLockEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[APP_LOCK_ENABLED] = enabled
        }
    }

    suspend fun setAppLockPin(pin: String) {
        dataStore.edit { preferences ->
            preferences[APP_LOCK_PIN] = pin
        }
    }
}