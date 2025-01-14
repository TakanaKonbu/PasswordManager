// ui/screens/SettingsScreen.kt
package com.takanakonbu.passwordmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController
) {
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
            Text("バックアップファイルは必ずGoogleDriveやDropboxと言ったサービスにアップロードしてください。\n" +
                    "機種を変更された際に新しい機種からファイルにアクセスできるようお願いします。")
        }
    }
}