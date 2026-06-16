package com.animesh.gitmate.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthState(
    val user: FirebaseUser? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    init {
        _state.value = _state.value.copy(user = auth.currentUser)
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _state.value = _state.value.copy(isLoading = false, user = auth.currentUser)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _state.value = _state.value.copy(isLoading = false, user = auth.currentUser)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _state.value = _state.value.copy(user = null)
    }
}