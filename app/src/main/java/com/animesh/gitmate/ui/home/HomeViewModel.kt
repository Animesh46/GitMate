package com.animesh.gitmate.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animesh.gitmate.data.api.GitHubUser
import com.animesh.gitmate.di.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val api = RetrofitClient.api
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState

    fun searchUser(username: String) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val user = api.getUser(username)
                _uiState.value = HomeUiState.Success(user)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "User not found")
            }
        }
    }
}