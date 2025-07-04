package com.tbart.toko.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tbart.toko.R
import com.tbart.toko.ui.theme.screen.HomeScreen
import com.tbart.toko.ui.theme.screen.PhotoScreen
import com.tbart.toko.ui.theme.screen.FollowsScreen
import com.tbart.toko.ui.theme.screen.ProfileScreen
import androidx.compose.foundation.layout.padding




@Composable
fun MainScreen() {
    var selectedIndex by remember { mutableStateOf(0) }

    val items = listOf(
        BottomNavItem("Parcourir", R.drawable.baseline_filter_24),
        BottomNavItem("Publier", R.drawable.baseline_photo_24),
        BottomNavItem("Abonnements", R.drawable.baseline_person_add_24),
        BottomNavItem("Profil", R.drawable.baseline_switch_account_24),
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = item.iconRes),
                                contentDescription = item.label.toString()
                            )
                        },
                        label = { (item.label) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFFFF9800),
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color(0xFFFF9800),
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { padding ->
        when (selectedIndex) {
            0 -> HomeScreen(modifier = Modifier.padding(padding))
            1 -> PhotoScreen(modifier = Modifier.padding(padding))
            2 -> FollowsScreen(modifier = Modifier.padding(padding))
            3 -> ProfileScreen(modifier = Modifier.padding(padding))
        }
    }
}


data class BottomNavItem(
    val label: String,
    val iconRes: Int
)

