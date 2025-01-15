package com.takanakonbu.passwordmanager

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.takanakonbu.passwordmanager.ui.navigation.Screen
import com.takanakonbu.passwordmanager.ui.screens.*
import com.takanakonbu.passwordmanager.ui.theme.PasswordManagerTheme
import com.takanakonbu.passwordmanager.ui.viewmodel.AccountViewModel
import com.takanakonbu.passwordmanager.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val accountViewModel: AccountViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初期化時のデバッグログ
        setupDebugLogging()

        setContent {
            PasswordManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // アプリロック関連の状態管理
                    val appLockEnabled by settingsViewModel.appLockEnabled.collectAsState()
                    val appLockPin by settingsViewModel.appLockPin.collectAsState()
                    var isAuthenticated by remember(appLockEnabled) { mutableStateOf(false) }

                    // アプリロックが無効の場合は自動的に認証状態にする
                    LaunchedEffect(appLockEnabled) {
                        if (!appLockEnabled) {
                            isAuthenticated = true
                        }
                    }

                    // PIN認証が必要な場合は認証画面を表示
                    if (appLockEnabled && !isAuthenticated && appLockPin != null) {
                        AuthScreen(
                            onAuthSuccess = {
                                isAuthenticated = true
                                Log.d("MainActivity", "Authentication successful")
                            },
                            correctPin = appLockPin ?: ""
                        )
                    } else {
                        // メインナビゲーション
                        MainNavigation()
                    }
                }
            }
        }
    }

    @Composable
    private fun MainNavigation() {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screen.AccountList.route
        ) {
            // アカウント一覧画面
            composable(Screen.AccountList.route) {
                AccountListScreen(
                    navController = navController,
                    viewModel = accountViewModel
                )
            }

            // アカウント追加画面
            composable(Screen.AddAccount.route) {
                AccountFormScreen(
                    navController = navController,
                    viewModel = accountViewModel
                )
            }

            // アカウント編集画面
            composable(
                route = Screen.EditAccount.route,
                arguments = listOf(
                    navArgument("accountId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                AccountFormScreen(
                    navController = navController,
                    viewModel = accountViewModel,
                    accountId = backStackEntry.arguments?.getLong("accountId")
                )
            }

            // 設定画面
            composable(Screen.Settings.route) {
                SettingsScreen(
                    navController = navController,
                    viewModel = settingsViewModel
                )
            }

            // パスワード生成画面
            composable(Screen.PasswordGenerator.route) {
                PasswordGeneratorScreen(
                    navController = navController
                )
            }
        }
    }

    private fun setupDebugLogging() {
        lifecycleScope.launch {
            settingsViewModel.appLockEnabled.collect { enabled ->
                Log.d("MainActivity", "AppLock Enabled: $enabled")
            }
        }
        lifecycleScope.launch {
            settingsViewModel.appLockPin.collect { pin ->
                Log.d("MainActivity", "AppLock PIN: $pin")
            }
        }
    }
}