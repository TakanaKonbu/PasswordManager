package com.takanakonbu.passwordmanager.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.takanakonbu.passwordmanager.data.backup.BackupManager
import com.takanakonbu.passwordmanager.data.datastore.SettingsDataStore
import com.takanakonbu.passwordmanager.data.db.AppDatabase
import com.takanakonbu.passwordmanager.data.repository.AccountRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsDataStore = SettingsDataStore(application)
    private val backupManager = BackupManager(application)
    private val repository: AccountRepository

    init {
        val accountDao = AppDatabase.getDatabase(application).accountDao()
        repository = AccountRepository(accountDao)
    }

    val appLockEnabled: StateFlow<Boolean> = settingsDataStore.appLockEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

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

    suspend fun createBackup(uri: Uri): Result<Unit> = runCatching {
        repository.getAllAccounts().first().let { accounts ->
            backupManager.createBackup(accounts, uri)
        }
    }

    suspend fun restoreBackup(uri: Uri): Result<Unit> = runCatching {
        backupManager.restoreBackup(uri).getOrThrow().let { restoredAccounts ->
            // 現在のアカウントを全て削除
            repository.getAllAccounts().first().forEach { account ->
                repository.deleteAccount(account)
            }
            // 復元したアカウントを保存
            restoredAccounts.forEach { account ->
                repository.insertAccount(account.copy(id = 0))  // IDを新規生成するために0にリセット
            }
        }
    }
}