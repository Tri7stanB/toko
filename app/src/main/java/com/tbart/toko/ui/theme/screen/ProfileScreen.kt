package com.tbart.toko.ui.theme.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tbart.toko.viewmodel.ProfileViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.tbart.toko.viewmodel.ProfileUiState
import androidx.compose.ui.text.input.PasswordVisualTransformation


@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel(), modifier: Modifier = Modifier) {
    val message by viewModel.message.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Récupération de l'état UI depuis le ViewModel
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Se connecter")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is ProfileUiState.Loading -> CircularProgressIndicator()
            is ProfileUiState.Success -> Text((state as ProfileUiState.Success).message, color = MaterialTheme.colorScheme.primary)
            is ProfileUiState.Error -> Text((state as ProfileUiState.Error).error, color = MaterialTheme.colorScheme.error)
            else -> {}
        }
    }
}