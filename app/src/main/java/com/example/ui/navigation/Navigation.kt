package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.CalamityDetailScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.screens.CivicIssueScreen
import androidx.compose.ui.platform.LocalContext
import com.example.data.UserPrefs
import androidx.compose.runtime.remember

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPrefs = remember { UserPrefs(context) }
    
    val startDestination = if (userPrefs.isLoggedIn()) "dashboard" else "welcome"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("welcome") {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate("login")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("dashboard") {
            DashboardScreen(
                onNavigateToCalamity = { calamityId ->
                    navController.navigate("calamity_detail/$calamityId")
                },
                onEditProfile = {
                    navController.navigate("login")
                },
                onNavigateToCivicLogger = {
                    navController.navigate("civic_logger")
                },
                onLogout = {
                    userPrefs.clearProfile()
                    navController.navigate("welcome") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(
            route = "calamity_detail/{calamityId}",
            arguments = listOf(navArgument("calamityId") { type = NavType.StringType })
        ) { backStackEntry ->
            val calamityId = backStackEntry.arguments?.getString("calamityId") ?: return@composable
            CalamityDetailScreen(
                calamityId = calamityId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("civic_logger") {
            CivicIssueScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
