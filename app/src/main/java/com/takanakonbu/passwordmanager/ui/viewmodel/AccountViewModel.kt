package com.takanakonbu.passwordmanager.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.takanakonbu.passwordmanager.data.db.AppDatabase
import com.takanakonbu.passwordmanager.data.model.AccountEntity
import com.takanakonbu.passwordmanager.data.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AccountRepository
    val allAccounts: StateFlow<List<AccountEntity>>
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // アカウント詳細を取得する関数を追加
    fun getAccountById(id: Long): Flow<AccountEntity?> = repository.getAccountById(id)

    init {
        val accountDao = AppDatabase.getDatabase(application).accountDao()
        repository = AccountRepository(accountDao)
        allAccounts = repository.getAllAccounts().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun insert(account: AccountEntity) = viewModelScope.launch {
        repository.insertAccount(account)
    }

    fun update(account: AccountEntity) = viewModelScope.launch {
        repository.updateAccount(account)
    }

    fun delete(account: AccountEntity) = viewModelScope.launch {
        repository.deleteAccount(account)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
