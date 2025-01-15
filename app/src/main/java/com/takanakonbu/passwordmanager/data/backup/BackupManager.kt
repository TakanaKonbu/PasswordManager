package com.takanakonbu.passwordmanager.data.backup

import android.content.Context
import android.net.Uri
import com.takanakonbu.passwordmanager.data.model.AccountEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class BackupData(
    val accounts: List<AccountEntity>,
    val backupDate: String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    val version: Int = 1  // バックアップフォーマットのバージョン
)

class BackupManager(private val context: Context) {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true  // デフォルト値も含めてシリアライズ
    }

    suspend fun createBackup(accounts: List<AccountEntity>, uri: Uri): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            val backupData = BackupData(accounts)
            val jsonString = json.encodeToString(backupData)

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.writer().use { writer ->
                    writer.write(jsonString)
                }
            } ?: throw IllegalStateException("Failed to open output stream for backup")
        }
    }

    suspend fun restoreBackup(uri: Uri): Result<List<AccountEntity>> = runCatching {
        withContext(Dispatchers.IO) {
            val jsonString = context.contentResolver.openInputStream(uri)?.bufferedReader()?.readText()
                ?: throw IllegalStateException("Failed to read backup file")

            val backupData = json.decodeFromString<BackupData>(jsonString)
            backupData.accounts
        }
    }
}