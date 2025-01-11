package com.takanakonbu.passwordmanager.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.takanakonbu.passwordmanager.data.db.AppDatabase
import com.takanakonbu.passwordmanager.data.model.AccountEntity
import com.takanakonbu.passwordmanager.data.repository.AccountRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AccountRepository
    private val _searchQuery = MutableStateFlow("")

    val allAccounts: StateFlow<List<AccountEntity>>
    val filteredAccounts: StateFlow<List<AccountEntity>>

    init {
        val accountDao = AppDatabase.getDatabase(application).accountDao()
        repository = AccountRepository(accountDao)

        allAccounts = repository.getAllAccounts().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        filteredAccounts = combine(allAccounts, _searchQuery) { accounts, query ->
            if (query.isEmpty()) {
                accounts
            } else {
                accounts.filter { account ->
                    account.serviceName.contains(query, ignoreCase = true) ||
                            account.loginId.contains(query, ignoreCase = true) ||
                            account.email.contains(query, ignoreCase = true)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun getAccountById(id: Long): Flow<AccountEntity?> = repository.getAccountById(id)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
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
}