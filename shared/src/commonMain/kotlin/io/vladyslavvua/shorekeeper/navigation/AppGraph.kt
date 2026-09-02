package io.vladyslavvua.shorekeeper.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.vladyslavvua.shorekeeper.feature.openedCef.OpenedCefScreen
import io.vladyslavvua.shorekeeper.feature.openedShore.OpenedShore
import io.vladyslavvua.shorekeeper.feature.settings.SettingsScreen
import io.vladyslavvua.shorekeeper.feature.welcome.WelcomeScreen

@Composable
fun AppGraph() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationPaths.Welcome) {
        composable<NavigationPaths.Welcome> {
            WelcomeScreen(navController)
        }
        composable<NavigationPaths.Welcome.Shore> { navBackStackEntry ->
            val params = navBackStackEntry.toRoute<NavigationPaths.Welcome.Shore>()
            OpenedShore(params.shoreId, navController)
        }
        composable<NavigationPaths.Settings> {
            SettingsScreen(navController)
        }

        composable <NavigationPaths.OpenCef>{
            OpenedCefScreen()
        }
    }
}