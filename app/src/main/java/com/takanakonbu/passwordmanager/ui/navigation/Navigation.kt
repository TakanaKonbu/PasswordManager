package com.takanakonbu.passwordmanager.ui.navigation

sealed class Screen(val route: String) {
    // Main screens
    data object AccountList : Screen("accountList")
    data object AddAccount : Screen("addAccount")
    data object EditAccount : Screen("editAccount/{accountId}") {
        fun createRoute(accountId: Long) = "editAccount/$accountId"
    }
    data object Settings : Screen("settings")
    data object PasswordGenerator : Screen("passwordGenerator")

    // Helper function to get all routes
    companion object {
        fun getMainRoutes() = listOf(
            AccountList.route,
            AddAccount.route,
            Settings.route,
            PasswordGenerator.route
        )
    }
}

// Navigation arguments
object NavArgs {
    const val ACCOUNT_ID = "accountId"
}

// Navigation deep links
object DeepLinks {
    const val SCHEME = "passwordmanager"
    const val HOST = "app"

    fun getDeepLinkUrl(screen: Screen): String {
        return "$SCHEME://$HOST/${screen.route}"
    }
}