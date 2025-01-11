package com.takanakonbu.passwordmanager.data.dao

import androidx.room.*
import com.takanakonbu.passwordmanager.data.model.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts ORDER BY serviceName ASC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    fun getAccountById(id: Long): Flow<AccountEntity?>

    @Insert
    suspend fun insertAccount(account: AccountEntity): Long

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Delete
    suspend fun deleteAccount(account: AccountEntity)

    @Query("SELECT * FROM accounts WHERE serviceName LIKE '%' || :query || '%' OR loginId LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%'")
    fun searchAccounts(query: String): Flow<List<AccountEntity>>
}