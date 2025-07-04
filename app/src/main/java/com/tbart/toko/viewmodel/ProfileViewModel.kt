package com.tbart.toko.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?> = _username

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadCurrentUser()
    }

    fun logout() {
        auth.signOut()
        _username.value = null
        _uiState.value = ProfileUiState.Idle
    }

    fun loadCurrentUser() {
        auth.currentUser?.email?.let { email ->
            firestore.collection("users")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnSuccessListener { snapshot ->
                    snapshot.documents.firstOrNull()?.let { doc ->
                        _username.value = doc.id
                    }
                }
        }
    }

    fun login(email: String, password: String) {
        _uiState.value = ProfileUiState.Loading

        viewModelScope.launch {
            if (email.isNotBlank() && password.isNotBlank()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            loadCurrentUser()
                            // Recherche par email plutôt que par UID
                            firestore.collection("users")
                                .whereEqualTo("email", email)
                                .limit(1)
                                .get()
                                .addOnSuccessListener { querySnapshot ->
                                    if (!querySnapshot.isEmpty) {
                                        val document = querySnapshot.documents[0]
                                        val username = document.getString("username") ?: "Inconnu"
                                        _uiState.value = ProfileUiState.Success("Bienvenue $username")
                                    } else {
                                        _uiState.value = ProfileUiState.Error("Utilisateur non trouvé dans Firestore.")
                                    }
                                }
                                .addOnFailureListener {
                                    _uiState.value = ProfileUiState.Error("Erreur Firestore : ${it.message}")
                                }
                        } else {
                            _uiState.value = ProfileUiState.Error(task.exception?.message ?: "Erreur inconnue")
                        }
                    }
            } else {
                _uiState.value = ProfileUiState.Error("Email ou mot de passe vide")
            }
        }
    }

    fun register(email: String, password: String, username: String) {
        _uiState.value = ProfileUiState.Loading

        if (email.isBlank() || password.isBlank() || username.isBlank()) {
            _uiState.value = ProfileUiState.Error("Tous les champs doivent être remplis")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val userMap = mapOf(
                            "email" to email,
                            "username" to username.lowercase(),
                            "follows" to emptyList<String>(),
                            "createdAt" to FieldValue.serverTimestamp(),
                        )
                        firestore.collection("users").document(username.lowercase()).set(userMap)
                            .addOnSuccessListener {
                                _uiState.value = ProfileUiState.Success("Compte créé avec succès. Bienvenue $username")
                            }
                            .addOnFailureListener { e ->
                                _uiState.value = ProfileUiState.Error("Erreur lors de l'enregistrement en base : ${e.message}")
                            }
                    } else {
                        _uiState.value = ProfileUiState.Error("UID utilisateur introuvable après création")
                    }
                } else {
                    _uiState.value = ProfileUiState.Error(task.exception?.message ?: "Erreur inconnue lors de la création du compte")
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