package com.tbart.toko.ui.theme.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tbart.toko.viewmodel.HomeViewModel

@Composable
fun ProfileScreen(viewModel: HomeViewModel = viewModel(), modifier: Modifier = Modifier) {
    val message by viewModel.message.collectAsState()

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = message)
    }
}
