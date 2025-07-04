package com.tbart.toko

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tbart.toko.ui.MainScreen
import com.tbart.toko.ui.theme.TokoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TokoTheme {
                MainScreen()
            }
        }
    }
}

