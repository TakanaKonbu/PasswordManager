package com.takanakonbu.passwordmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.takanakonbu.passwordmanager.data.model.AccountEntity
import com.takanakonbu.passwordmanager.ui.viewmodel.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountFormScreen(
    navController: NavController,
    viewModel: AccountViewModel,
    accountId: Long? = null
) {
    var serviceName by remember { mutableStateOf("") }
    var loginId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (accountId == null) "アカウント追加" else "アカウント編集") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = serviceName,
                onValueChange = { serviceName = it },
                label = { Text("ログイン先") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = loginId,
                onValueChange = { loginId = it },
                label = { Text("ログインID") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("メールアドレス") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("パスワード") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("備考") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val account = AccountEntity(
                        id = accountId ?: 0,
                        serviceName = serviceName,
                        loginId = loginId,
                        email = email,
                        password = password,
                        note = note
                    )
                    if (accountId == null) {
                        viewModel.insert(account)
                    } else {
                        viewModel.update(account)
                    }
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (accountId == null) "追加" else "更新")
            }
        }
    }
}