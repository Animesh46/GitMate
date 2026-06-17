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
import com.animesh.gitmate.ui.browser.RepoFileBrowserScreen
import com.animesh.gitmate.ui.explainer.FileExplainerScreen

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
                                onRepoClick = { repoName ->
                                    navController.navigate("browser/$username/$repoName")
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable(
                            "browser/{owner}/{repo}",
                            arguments = listOf(
                                navArgument("owner") { type = NavType.StringType },
                                navArgument("repo") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val owner = backStackEntry.arguments?.getString("owner") ?: ""
                            val repo = backStackEntry.arguments?.getString("repo") ?: ""
                            RepoFileBrowserScreen(
                                owner = owner,
                                repo = repo,
                                onFileClick = { filePath ->
                                    // Navigate to the explainer screen
                                    navController.navigate("explain/$owner/$repo/${filePath.replace("/", "_")}")
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            "explain/{owner}/{repo}/{path}",
                            arguments = listOf(
                                navArgument("owner") { type = NavType.StringType },
                                navArgument("repo") { type = NavType.StringType },
                                navArgument("path") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val owner = backStackEntry.arguments?.getString("owner") ?: ""
                            val repo = backStackEntry.arguments?.getString("repo") ?: ""
                            val path = backStackEntry.arguments?.getString("path") ?: ""
                            // Decode the path (replace "_" with "/" – we used replace to avoid URL issues)
                            val decodedPath = path.replace("_", "/")
                            FileExplainerScreen(
                                owner = owner,
                                repo = repo,
                                path = decodedPath,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}