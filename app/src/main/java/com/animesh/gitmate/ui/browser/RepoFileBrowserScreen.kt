package com.animesh.gitmate.ui.browser

import androidx.compose.foundation.clickable
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
import com.animesh.gitmate.data.api.GitHubContentItem
import com.animesh.gitmate.di.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FileBrowserViewModel : ViewModel() {
    private val api = RetrofitClient.api

    private val _items = MutableStateFlow<List<GitHubContentItem>>(emptyList())
    val items: StateFlow<List<GitHubContentItem>> = _items

    private val _currentPath = MutableStateFlow("")
    val currentPath: StateFlow<String> = _currentPath

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var owner: String = ""
    private var repo: String = ""

    fun loadRepo(owner: String, repo: String, path: String = "") {
        this.owner = owner
        this.repo = repo
        loadPath(path)
    }

    fun loadPath(path: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = api.getRepoContents(owner, repo, path)
                _items.value = result
                _currentPath.value = path
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load files"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun navigateToItem(item: GitHubContentItem) {
        if (item.type == "dir") {
            loadPath(item.path)
        }
    }

    fun navigateUp() {
        val path = _currentPath.value
        val lastSlash = path.lastIndexOf('/')
        if (lastSlash > 0) {
            loadPath(path.substring(0, lastSlash))
        } else if (path.isNotEmpty()) {
            loadPath("")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoFileBrowserScreen(
    owner: String,
    repo: String,
    onFileClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: FileBrowserViewModel = viewModel()
) {
    val items by viewModel.items.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentPath by viewModel.currentPath.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRepo(owner, repo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$repo / ${currentPath.ifEmpty { "root" }}") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("← Back") }
                },
                actions = {
                    if (currentPath.isNotEmpty()) {
                        TextButton(onClick = { viewModel.navigateUp() }) {
                            Text("↑ Up")
                        }
                    }
                }
            )
        }
    ) { padding ->
        when {
            isLoading -> Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            error != null -> Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }
            else -> LazyColumn(modifier = Modifier.padding(padding)) {
                items(items) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (item.type == "dir") {
                                    viewModel.navigateToItem(item)
                                } else {
                                    onFileClick(item.path)
                                }
                            }
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "${if (item.type == "dir") "📁 " else "📄 "} ${item.name}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (item.type == "file") {
                            Text("${item.size} bytes", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Divider()
                }
            }
        }
    }
}