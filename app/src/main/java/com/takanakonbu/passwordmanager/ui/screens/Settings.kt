package com.takanakonbu.passwordmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.takanakonbu.passwordmanager.ui.components.PinInputDialog
import com.takanakonbu.passwordmanager.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = viewModel()
) {
    val appLockEnabled by viewModel.appLockEnabled.collectAsState()
    var showPinDialog by remember { mutableStateOf(false) }

    if (showPinDialog) {
        PinInputDialog(
            title = "PINを設定",
            onDismiss = { showPinDialog = false },
            onConfirm = { pin ->
                viewModel.setAppLockPin(pin)
                viewModel.setAppLockEnabled(true)
                showPinDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("設定") },
                Modifier
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🔑 アプリ起動時にパスワードを使用する")
                Switch(
                    checked = appLockEnabled,
                    onCheckedChange = { enabled ->
                        if (enabled) {
                            showPinDialog = true
                        } else {
                            viewModel.setAppLockEnabled(false)
                        }
                    }
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Text("🔐パスワードを生成する")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Text("⬆️バックアップファイルの作成")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Text("⬇️バックアップファイルの復元")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Text("📝プライバシーポリシー")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Text("⚠️注意事項")
            Text(
                "バックアップファイルは必ずGoogleDriveやDropboxと言ったサービスにアップロードしてください。\n" +
                        "機種を変更された際に新しい機種からファイルにアクセスできるようお願いします。"
            )

            if (appLockEnabled) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "PIN認証が有効です",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}