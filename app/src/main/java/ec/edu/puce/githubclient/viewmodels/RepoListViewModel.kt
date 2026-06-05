package ec.edu.puce.githubclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.puce.githubclient.models.Repository
import ec.edu.puce.githubclient.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepoListViewModel : ViewModel() {
    private val _repos = MutableStateFlow<List<Repository>>(value = emptyList())
    val repos: StateFlow<List<Repository>> = _repos.asStateFlow()

    private val _isLoading = MutableStateFlow(value = false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(value = null)
    val errMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    private val _isDeleteSuccess = MutableStateFlow(value = false)
    val isDeleteSuccess: StateFlow<Boolean> = _isDeleteSuccess.asStateFlow()

    init {
        fetchRepos()
    }

    fun fetchRepos() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                _repos.value = RetrofitClient.apiService.getRepositories()
            } catch (e: Exception) {
                _errorMsg.value = "Error al cargar repositorios: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // UNA SOLA FUNCIÓN UNIFICADA CON EL ÉXITO Y EL FETCH
    fun deleteRepository(owner: String, repo: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null

            println("OWNER = $owner")
            println("REPO = $repo")

            try {
                val response = RetrofitClient.apiService.deleteRepository(
                    owner = owner,
                    repo = repo
                )

                if (response.isSuccessful) {
                    _isDeleteSuccess.value = true // Activa el Snackbar
                    fetchRepos() // Recarga la lista
                } else {
                    _errorMsg.value = "Error al eliminar: Código ${response.code()} - ${response.message()}"
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _errorMsg.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetDeleteSuccess() {
        _isDeleteSuccess.value = false
    }
}