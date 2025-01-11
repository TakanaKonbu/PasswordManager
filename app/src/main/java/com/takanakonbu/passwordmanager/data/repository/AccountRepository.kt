package com.takanakonbu.passwordmanager.data.repository

import com.takanakonbu.passwordmanager.data.dao.AccountDao
import com.takanakonbu.passwordmanager.data.model.AccountEntity
import kotlinx.coroutines.flow.Flow
class AccountRepository(
    private val accountDao: AccountDao
) {
    fun getAllAccounts(): Flow<List<AccountEntity>> = accountDao.getAllAccounts()

    fun getAccountById(id: Long): Flow<AccountEntity?> = accountDao.getAccountById(id)

    fun searchAccounts(query: String): Flow<List<AccountEntity>> = accountDao.searchAccounts(query)

    suspend fun insertAccount(account: AccountEntity): Long = accountDao.insertAccount(account)

    suspend fun updateAccount(account: AccountEntity) = accountDao.updateAccount(account)

    suspend fun deleteAccount(account: AccountEntity) = accountDao.deleteAccount(account)
}