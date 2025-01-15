package com.takanakonbu.passwordmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.takanakonbu.passwordmanager.ui.components.PinInputDialog

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    correctPin: String
) {
    val showPinDialog by remember { mutableStateOf(true) }

    if (showPinDialog) {
        PinInputDialog(
            title = "PINを入力",
            onDismiss = { /* PINの入力は必須なのでdismissは無効 */ },
            onConfirm = { pin ->
                if (pin == correctPin) {
                    onAuthSuccess()
                }
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "PIN認証が必要です",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}