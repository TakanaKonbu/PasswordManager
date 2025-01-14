package com.takanakonbu.passwordmanager.ui.navigation

sealed class Screen(val route: String) {
    data object AccountList : Screen("accountList")
    data object AddAccount : Screen("addAccount")
    data object EditAccount : Screen("editAccount/{accountId}") {
        fun createRoute(accountId: Long) = "editAccount/$accountId"
    }
    // Navigation.kt の Screen クラス内に追加
    data object Settings : Screen("settings")
}