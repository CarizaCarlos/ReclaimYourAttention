package com.reclaimyourattention.ui.ToolsScreens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

@Composable
fun NavigationBar(navController: NavController?=null) {
    val navItems = listOf(
        NavItem("Principal", "main", Icons.Default.Home),
        NavItem("Herramientas", "tools", Icons.Default.Build),
        NavItem("Uso", "usage", Icons.Default.Analytics)
    )

    val currentRoute = navController?.currentBackStackEntry?.destination?.route

    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController?.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

data class NavItem(
    val label: String,
    val route: String,
    val icon: ImageVector,
)