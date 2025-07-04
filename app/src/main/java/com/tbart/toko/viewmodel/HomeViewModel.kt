package com.tbart.toko.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _message = MutableStateFlow("Bienvenue sur l'accueil")
    val message: StateFlow<String> = _message
}
