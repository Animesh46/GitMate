package com.animesh.gitmate.ui.explainer

import android.util.Base64
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.animesh.gitmate.BuildConfig
import com.animesh.gitmate.di.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

class FileExplainerViewModel : ViewModel() {
    private val api = RetrofitClient.api
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()

    private val _fileContent = MutableStateFlow<String?>(null)
    val fileContent: StateFlow<String?> = _fileContent

    private val _aiExplanation = MutableStateFlow<String?>(null)
    val aiExplanation: StateFlow<String?> = _aiExplanation

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var owner = ""
    private var repo = ""
    private var filePath = ""

    fun loadFile(owner: String, repo: String, path: String) {
        this.owner = owner
        this.repo = repo
        this.filePath = path

        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null
            try {
                val result = api.getFileContent(owner, repo, path)
                val decoded = decodeBase64(result.content)
                _fileContent.value = decoded
                explainFile(decoded)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load file"
                _isLoading.value = false
            }
        }
    }

    private fun decodeBase64(content: String): String {
        return try {
            val decodedBytes = Base64.decode(content, Base64.DEFAULT)
            String(decodedBytes, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            "Error decoding file: ${e.message}"
        }
    }

    private fun explainFile(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val apiKey = BuildConfig.NVIDIA_API_KEY
            if (apiKey.isEmpty() || apiKey == "") {
                withContext(Dispatchers.Main) {
                    _aiExplanation.value = "⚠️ Add NVIDIA_API_KEY to local.properties"
                    _isLoading.value = false
                }
                return@launch
            }

            val fileName = filePath.substringAfterLast('/')
            val prompt = "Explain this code file from GitHub repo '$owner/$repo'.\n\n" +
                    "File: $fileName\n" +
                    "Path: $filePath\n\n" +
                    "Code:\n```kotlin\n$code\n```\n\n" +
                    "Explain what this file does, its main purpose, and any key functions or patterns. Keep it clear and beginner-friendly."

            val requestBody = mapOf(
                "model" to "deepseek-ai/deepseek-v4-pro",
                "messages" to listOf(
                    mapOf("role" to "system", "content" to "You are a helpful code explainer."),
                    mapOf("role" to "user", "content" to prompt)
                ),
                "stream" to false
            )

            val json = gson.toJson(requestBody)
            val body = json.toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("https://integrate.api.nvidia.com/v1/chat/completions")
                .header("Authorization", "Bearer $apiKey")
                .post(body)
                .build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val jsonResponse = gson.fromJson(responseBody, Map::class.java)
                    @Suppress("UNCHECKED_CAST")
                    val choices = jsonResponse["choices"] as? List<Map<String, Any>>
                    val message = choices?.get(0)?.get("message") as? Map<String, Any>
                    val content = message?.get("content") as? String
                    withContext(Dispatchers.Main) {
                        _aiExplanation.value = content ?: "No response from AI"
                        _isLoading.value = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _aiExplanation.value = "AI error: ${response.code}"
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _aiExplanation.value = "Network error: ${e.javaClass.simpleName} - ${e.message}"
                    _isLoading.value = false
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileExplainerScreen(
    owner: String,
    repo: String,
    path: String,
    onBack: () -> Unit,
    viewModel: FileExplainerViewModel = viewModel()
) {
    val fileContent by viewModel.fileContent.collectAsState()
    val aiExplanation by viewModel.aiExplanation.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFile(owner, repo, path)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(path.substringAfterLast('/')) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("← Back") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Text("Loading file...")
                    }
                }
            } else if (error != null) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            } else {
                Text("📄 File Preview", style = MaterialTheme.typography.titleMedium)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = fileContent?.take(300) ?: "No content",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("🧠 AI Explanation", style = MaterialTheme.typography.titleMedium)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = aiExplanation ?: "Waiting for AI explanation...",
                        modifier = Modifier
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState()),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}