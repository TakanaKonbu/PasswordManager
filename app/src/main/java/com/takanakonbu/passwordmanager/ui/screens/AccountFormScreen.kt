package com.takanakonbu.passwordmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.takanakonbu.passwordmanager.data.model.AccountEntity
import com.takanakonbu.passwordmanager.ui.theme.PrimaryColor
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

    // 編集時にアカウント情報を読み込む
    LaunchedEffect(accountId) {
        accountId?.let { id ->
            viewModel.getAccountById(id).collect { account ->
                account?.let {
                    serviceName = it.serviceName
                    loginId = it.loginId
                    email = it.email
                    password = it.password
                    note = it.note
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (accountId == null) "アカウント追加" else "アカウント編集") },
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
            OutlinedTextField(
                value = serviceName,
                onValueChange = { serviceName = it },
                label = { Text("ログイン先") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = loginId,
                onValueChange = { loginId = it },
                label = { Text("ログインID") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,  // メール用のキーボード（@マークが入力しやすい）
                    imeAction = ImeAction.Next          // 右下のボタンで次の入力欄へ
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("メールアドレス") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,  // メール用のキーボード（@マークが入力しやすい）
                    imeAction = ImeAction.Next          // 右下のボタンで次の入力欄へ
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("パスワード") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,  // パスワード用のキーボード
                    imeAction = ImeAction.Next            // 右下のボタンで次の入力欄へ
                )
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
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor,
                    contentColor = Color.White
                )
            ) {
                Text(if (accountId == null) "追加" else "更新")
            }
        }
    }
}