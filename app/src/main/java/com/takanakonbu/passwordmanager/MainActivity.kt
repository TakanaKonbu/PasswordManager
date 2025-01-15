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
import com.takanakonbu.passwordmanager.ui.screens.AccountFormScreen
import com.takanakonbu.passwordmanager.ui.screens.AccountListScreen
import com.takanakonbu.passwordmanager.ui.screens.AuthScreen
import com.takanakonbu.passwordmanager.ui.screens.SettingsScreen
import com.takanakonbu.passwordmanager.ui.theme.PasswordManagerTheme
import com.takanakonbu.passwordmanager.ui.viewmodel.AccountViewModel
import com.takanakonbu.passwordmanager.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: AccountViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // デバッグ用のログ出力
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

        setContent {
            PasswordManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val appLockEnabled by settingsViewModel.appLockEnabled.collectAsState()
                    val appLockPin by settingsViewModel.appLockPin.collectAsState()
                    var isAuthenticated by remember(appLockEnabled) { mutableStateOf(false) }

                    LaunchedEffect(appLockEnabled) {
                        if (!appLockEnabled) {
                            isAuthenticated = true
                        }
                    }

                    if (appLockEnabled && !isAuthenticated && appLockPin != null) {
                        val nonNullPin = appLockPin ?: ""
                        AuthScreen(
                            onAuthSuccess = {
                                isAuthenticated = true
                                Log.d("MainActivity", "Authentication successful")
                            },
                            correctPin = nonNullPin
                        )
                    } else {
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = Screen.AccountList.route
                        ) {
                            composable(Screen.AccountList.route) {
                                AccountListScreen(
                                    navController = navController,
                                    viewModel = viewModel
                                )
                            }
                            composable(Screen.AddAccount.route) {
                                AccountFormScreen(
                                    navController = navController,
                                    viewModel = viewModel
                                )
                            }
                            composable(
                                route = Screen.EditAccount.route,
                                arguments = listOf(
                                    navArgument("accountId") { type = NavType.LongType }
                                )
                            ) { backStackEntry ->
                                AccountFormScreen(
                                    navController = navController,
                                    viewModel = viewModel,
                                    accountId = backStackEntry.arguments?.getLong("accountId")
                                )
                            }
                            composable(Screen.Settings.route) {
                                SettingsScreen(
                                    navController = navController,
                                    viewModel = settingsViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}