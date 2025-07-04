import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FollowsViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    // États pour la recherche
    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> = _searchResults

    // États pour les erreurs/chargement
    private val _uiState = MutableStateFlow<FollowsUiState>(FollowsUiState.Idle)
    val uiState: StateFlow<FollowsUiState> = _uiState

    // Recherche d'utilisateurs
    fun searchUsers(query: String) {
        if (query.length < 3) {
            _searchResults.value = emptyList()
            return
        }

        _uiState.value = FollowsUiState.Loading

        viewModelScope.launch {
            firestore.collection("users")
                .orderBy("username")
                .startAt(query)
                .endAt("$query\uf8ff")
                .limit(10)
                .get()
                .addOnSuccessListener { results ->
                    val users = results.map { doc ->
                        User(
                            id = doc.id,
                            username = doc.getString("username") ?: "",
                            email = doc.getString("email") ?: ""
                        )
                    }
                    _searchResults.value = users
                    _uiState.value = FollowsUiState.Success
                }
                .addOnFailureListener {
                    _uiState.value = FollowsUiState.Error("Erreur de recherche")
                }
        }
    }

    // Suivre un utilisateur
    fun followUser(currentUserId: String, userToFollowId: String) {
        _uiState.value = FollowsUiState.Loading

        firestore.collection("users")
            .document(currentUserId)
            .collection("following")
            .document(userToFollowId)
            .set(mapOf("timestamp" to System.currentTimeMillis()))
            .addOnSuccessListener {
                _uiState.value = FollowsUiState.Success
            }
            .addOnFailureListener {
                _uiState.value = FollowsUiState.Error("Erreur lors du suivi")
            }
    }
}

data class User(
    val id: String,
    val username: String,
    val email: String
)

sealed class FollowsUiState {
    object Idle : FollowsUiState()
    object Loading : FollowsUiState()
    object Success : FollowsUiState()
    data class Error(val message: String) : FollowsUiState()
}