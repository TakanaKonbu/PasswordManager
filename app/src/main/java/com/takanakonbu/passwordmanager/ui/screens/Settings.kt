package com.takanakonbu.passwordmanager.ui.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.takanakonbu.passwordmanager.ui.components.PinInputDialog
import com.takanakonbu.passwordmanager.ui.navigation.Screen
import com.takanakonbu.passwordmanager.ui.theme.PrimaryColor
import com.takanakonbu.passwordmanager.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val appLockEnabled by viewModel.appLockEnabled.collectAsState()
    var showPinDialog by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf<String?>(null) }

    // エラーメッセージ表示用のSnackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(showErrorMessage) {
        showErrorMessage?.let {
            snackbarHostState.showSnackbar(it)
            showErrorMessage = null
        }
    }

    // バックアップファイル作成のランチャー
    val backupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            scope.launch {
                viewModel.createBackup(uri)
                    .onSuccess {
                        snackbarHostState.showSnackbar("バックアップを作成しました")
                    }
                    .onFailure { e ->
                        showErrorMessage = "バックアップの作成に失敗しました: ${e.message}"
                    }
            }
        }
    }

    // バックアップファイル復元のランチャー
    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            scope.launch {
                viewModel.restoreBackup(uri)
                    .onSuccess {
                        snackbarHostState.showSnackbar("バックアップを復元しました")
                    }
                    .onFailure { e ->
                        showErrorMessage = "バックアップの復元に失敗しました: ${e.message}"
                    }
            }
        }
    }

    if (showPinDialog) {
        PinInputDialog(
            title = "PINを設定",
            onDismiss = { showPinDialog = false },
            onConfirm = { pin ->
                if (pin.length == 6) {
                    viewModel.setAppLockPin(pin)
                    viewModel.setAppLockEnabled(true)
                    showPinDialog = false
                } else {
                    showErrorMessage = "PINは6桁で入力してください"
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("設定") },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .shadow(elevation = 4.dp),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // セキュリティ設定
            SettingsSectionTitle(text = "セキュリティ")
            SettingsItem(
                icon = "🔑",
                title = "アプリ起動時にパスワードを使用する",
                onClick = null // Switchがあるので項目全体のクリックは無効
            ) {
                Switch(
                    checked = appLockEnabled,
                    onCheckedChange = { enabled ->
                        if (enabled) {
                            showPinDialog = true
                        } else {
                            viewModel.setAppLockEnabled(false)
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = PrimaryColor,
                        checkedTrackColor = PrimaryColor.copy(alpha = 0.5f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ツール
            SettingsSectionTitle(text = "ツール")
            SettingsItem(
                icon = "🔐",
                title = "パスワードを生成する",
                onClick = { navController.navigate(Screen.PasswordGenerator.route) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // バックアップ
            SettingsSectionTitle(text = "バックアップ")
            SettingsItem(
                icon = "⬆️",
                title = "バックアップファイルの作成",
                onClick = {
                    backupLauncher.launch("password-manager-backup-${System.currentTimeMillis()}.json")
                }
            )
            SettingsItem(
                icon = "⬇️",
                title = "バックアップファイルの復元",
                onClick = {
                    restoreLauncher.launch("application/json")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // その他
            SettingsSectionTitle(text = "その他")
            SettingsItem(
                icon = "📝",
                title = "プライバシーポリシー",
                onClick = { /* TODO: プライバシーポリシー画面実装 */ }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 注意事項
            Text(
                text = "⚠️ 注意事項",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "バックアップファイルは必ずGoogleDriveやDropboxといったクラウドストレージにアップロードしてください。\n" +
                        "機種変更時に新しい機種からファイルにアクセスできるようお願いします。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // アプリロック状態の表示
            if (appLockEnabled) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "PIN認証が有効です",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun SettingsItem(
    icon: String,
    title: String,
    onClick: (() -> Unit)?,
    trailing: @Composable (() -> Unit)? = {
        if (onClick != null) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = icon,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            trailing?.invoke()
        }
    }
}