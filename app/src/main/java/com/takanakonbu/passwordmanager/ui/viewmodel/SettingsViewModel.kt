package com.takanakonbu.passwordmanager.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.takanakonbu.passwordmanager.data.datastore.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsDataStore = SettingsDataStore(application)

    // stateInのinitialValueをfalseに明示的に設定
    val appLockEnabled: StateFlow<Boolean> = settingsDataStore.appLockEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // nullableな文字列型として明示的に型指定
    val appLockPin: StateFlow<String?> = settingsDataStore.appLockPin
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun setAppLockEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setAppLockEnabled(enabled)
        }
    }

    fun setAppLockPin(pin: String) {
        viewModelScope.launch {
            settingsDataStore.setAppLockPin(pin)
        }
    }
}