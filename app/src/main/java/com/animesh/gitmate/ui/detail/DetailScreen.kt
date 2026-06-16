package com.animesh.gitmate.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.animesh.gitmate.data.api.GitHubRepo
import com.animesh.gitmate.data.api.GitHubUser
import com.animesh.gitmate.di.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val username: String) : ViewModel() {
    private val api = RetrofitClient.api

    private val _user = MutableStateFlow<GitHubUser?>(null)
    val user: StateFlow<GitHubUser?> = _user

    private val _repos = MutableStateFlow<List<GitHubRepo>>(emptyList())
    val repos: StateFlow<List<GitHubRepo>> = _repos

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _user.value = api.getUser(username)
                _repos.value = api.getUserRepos(username)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class DetailViewModelFactory(private val username: String) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(username) as T
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    username: String,
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel(factory = DetailViewModelFactory(username))
) {
    val user by viewModel.user.collectAsState()
    val repos by viewModel.repos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(username) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("← Back") }
                }
            )
        }
    ) { padding ->
        when {
            isLoading -> Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            error != null -> Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: ${error!!}", color = MaterialTheme.colorScheme.error)
            }
            user != null -> LazyColumn(
                modifier = Modifier.padding(padding).padding(16.dp)
            ) {
                item {
                    Text(user!!.login, style = MaterialTheme.typography.headlineSmall)
                    Text(user!!.bio ?: "No bio", style = MaterialTheme.typography.bodyMedium)
                    Text("Followers: ${user!!.followers}  •  Following: ${user!!.following}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Repositories", style = MaterialTheme.typography.titleMedium)
                }
                items(repos) { repo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(repo.name, style = MaterialTheme.typography.titleSmall)
                            repo.description?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                            Row {
                                repo.language?.let { Text("Language: $it") }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("⭐ ${repo.stars}")
                            }
                        }
                    }
                }
            }
        }
    }
}