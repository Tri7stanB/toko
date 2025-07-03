package com.tbart.toko

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedIndex by remember { mutableStateOf(0) }

    val items = listOf("Home", "Profile", "Photo", "Follows")

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { /* ici tu peux mettre Icon(...) */ },
                        label = { Text(item) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        when (selectedIndex) {
            0 -> HomeScreen(Modifier.padding(paddingValues))
            1 -> ProfileScreen(Modifier.padding(paddingValues))
            2 -> PhotoScreen(Modifier.padding(paddingValues))
            3 -> FollowsScreen(Modifier.padding(paddingValues))
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Text(text = "Home Screen", modifier = modifier)
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Text(text = "Profile Screen", modifier = modifier)
}

@Composable
fun PhotoScreen(modifier: Modifier = Modifier) {
    Text(text = "Photo Screen", modifier = modifier)
}

@Composable
fun FollowsScreen(modifier: Modifier = Modifier) {
    Text(text = "Follows Screen", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TokoTheme {
        MainScreen()
    }
}
