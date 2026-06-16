package com.animesh.gitmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.animesh.gitmate.ui.auth.AuthViewModel
import com.animesh.gitmate.ui.auth.LoginScreen
import com.animesh.gitmate.ui.detail.DetailScreen
import com.animesh.gitmate.ui.home.HomeScreen
import com.animesh.gitmate.ui.theme.GitMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitMateTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val authViewModel = AuthViewModel()

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                viewModel = authViewModel
                            )
                        }
                        composable("home") {
                            HomeScreen(
                                onLogout = {
                                    authViewModel.signOut()
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                },
                                onUserClick = { username ->
                                    navController.navigate("detail/$username")
                                }
                            )
                        }
                        composable(
                            "detail/{username}",
                            arguments = listOf(navArgument("username") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val username = backStackEntry.arguments?.getString("username") ?: ""
                            DetailScreen(
                                username = username,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}