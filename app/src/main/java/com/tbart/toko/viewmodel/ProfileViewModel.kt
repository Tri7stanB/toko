package com.tbart.toko.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _message = MutableStateFlow("Bienvenue sur le profil")
    val message: StateFlow<String> = _message

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun login(email: String, password: String) {
        _uiState.value = ProfileUiState.Loading

        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _uiState.value = if (task.isSuccessful) {
                        ProfileUiState.Success("Connecté en tant que ${auth.currentUser?.email}")
                    } else {
                        ProfileUiState.Error(task.exception?.message ?: "Erreur inconnue")
                    }
                }
        }
    }
}

sealed class ProfileUiState {
    object Idle : ProfileUiState()
    object Loading : ProfileUiState()
    data class Success(val message: String) : ProfileUiState()
    data class Error(val error: String) : ProfileUiState()
}