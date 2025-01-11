package com.takanakonbu.passwordmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val serviceName: String,        // ログイン先
    val loginId: String,           // ログインID
    val email: String,             // 登録メールアドレス
    val password: String,          // パスワード
    val note: String = "",         // 備考
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)