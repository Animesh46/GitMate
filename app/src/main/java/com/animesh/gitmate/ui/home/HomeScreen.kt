package com.animesh.gitmate.ui.home

import com.animesh.gitmate.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onUserClick: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GitMate") },
                actions = {
                    TextButton(onClick = onLogout) { Text("Logout") }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("GitHub Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { viewModel.searchUser(username) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }

            when (val s = state) {
                is HomeUiState.Loading -> CircularProgressIndicator()
                is HomeUiState.Success -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onUserClick(s.user.login) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(s.user.login, style = MaterialTheme.typography.titleMedium)
                            Text(s.user.bio ?: "No bio", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                is HomeUiState.Error -> {
                    Text(s.message, color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.searchUser(username) }) { Text("Retry") }
                }
                else -> {}
            }
        }
    }
}

sealed class HomeUiState {
    object Idle : HomeUiState()
    object Loading : HomeUiState()
    data class Success(val user: com.animesh.gitmate.data.api.GitHubUser) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}