package com.takanakonbu.passwordmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.takanakonbu.passwordmanager.ui.theme.PrimaryColor
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordGeneratorScreen(
    navController: NavController
) {
    var passwordLength by remember { mutableStateOf("12") }
    var includeSymbols by remember { mutableStateOf(false) }
    var generatedPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("パスワード生成") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = passwordLength,
                onValueChange = {
                    if (it.isEmpty() || (it.toIntOrNull() ?: 0) <= 100) {
                        passwordLength = it
                    }
                },
                label = { Text("パスワードの長さ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = includeSymbols,
                    onCheckedChange = { includeSymbols = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = PrimaryColor,
                        uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text("記号を含める (!@#$%^&*())")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    generatedPassword = generatePassword(
                        length = passwordLength.toIntOrNull() ?: 12,
                        includeSymbols = includeSymbols
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor,
                    contentColor = Color.White
                )
            ) {
                Text("パスワードを生成")
            }

            if (generatedPassword.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "生成されたパスワード:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = generatedPassword,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }
        }
    }
}

private fun generatePassword(length: Int, includeSymbols: Boolean): String {
    val upperCase = ('A'..'Z').toList()
    val lowerCase = ('a'..'z').toList()
    val numbers = ('0'..'9').toList()
    val symbols = "!@#$%^&*()".toList()

    val allChars = mutableListOf<Char>().apply {
        addAll(upperCase)
        addAll(lowerCase)
        addAll(numbers)
        if (includeSymbols) addAll(symbols)
    }

    return buildString {
        // 必須文字を追加
        append(upperCase.random())
        append(lowerCase.random())
        append(numbers.random())
        if (includeSymbols) append(symbols.random())

        // 残りの文字をランダムに追加
        repeat(length - if(includeSymbols) 4 else 3) {
            append(allChars[Random.nextInt(allChars.size)])
        }
    }.toList().shuffled().joinToString("")
}