package com.takanakonbu.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.takanakonbu.passwordmanager.ui.navigation.Screen
import com.takanakonbu.passwordmanager.ui.screens.AccountFormScreen
import com.takanakonbu.passwordmanager.ui.screens.AccountListScreen
import com.takanakonbu.passwordmanager.ui.theme.PasswordManagerTheme
import com.takanakonbu.passwordmanager.ui.viewmodel.AccountViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PasswordManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.AccountList.route
                    ) {
                        composable(Screen.AccountList.route) {
                            AccountListScreen(navController, viewModel)
                        }
                        composable(Screen.AddAccount.route) {
                            AccountFormScreen(navController, viewModel)
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
                    }
                }
            }
        }
    }
}