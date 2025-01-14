package com.takanakonbu.passwordmanager.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.takanakonbu.passwordmanager.data.model.AccountEntity
import com.takanakonbu.passwordmanager.ui.navigation.Screen
import com.takanakonbu.passwordmanager.ui.theme.PrimaryColor
import com.takanakonbu.passwordmanager.ui.viewmodel.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountListScreen(
    navController: NavController,
    viewModel: AccountViewModel
) {
    val accounts by viewModel.filteredAccounts.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            // AccountListScreen.kt のColumnブロックを修正
            Column {
                TopAppBar(
                    title = { Text("パスワードマネージャー") },
                    Modifier
                        .padding(bottom = 8.dp)
                        .shadow(elevation = 4.dp),
                    actions = {
                        IconButton(
                            onClick = { navController.navigate(Screen.Settings.route) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = PrimaryColor
                            )
                        }
                    }
                )
                SearchBar(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                        viewModel.updateSearchQuery(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("AddAccount") },
                containerColor = PrimaryColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Account")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(accounts) { account ->
                AccountItem(
                    account = account,
                    onItemClick = { navController.navigate(Screen.EditAccount.createRoute(account.id)) },
                    onDeleteClick = { viewModel.delete(account) }
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("検索") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun AccountItem(
    account: AccountEntity,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .clickable(onClick = onItemClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF6F1FF)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = account.serviceName,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}