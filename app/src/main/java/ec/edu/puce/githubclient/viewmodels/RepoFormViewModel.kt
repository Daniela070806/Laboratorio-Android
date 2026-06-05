package ec.edu.puce.githubclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.puce.githubclient.models.Repository
import ec.edu.puce.githubclient.models.RepositoryPayload
import ec.edu.puce.githubclient.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepoFormViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(value = false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSuccess = MutableStateFlow(value = false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(value = null)
    val errMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    // Variable para retener el repositorio que se va a editar
    private val _repoToEdit = MutableStateFlow<Repository?>(null)
    val repoToEdit: StateFlow<Repository?> = _repoToEdit.asStateFlow()

    // Configura el modo edición
    fun setRepositoryToEdit(repository: Repository?) {
        _repoToEdit.value = repository
    }

    // Método para CREAR un repositorio
    fun createRepository(name: String, description: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val payload = RepositoryPayload(name = name, description = description)
                RetrofitClient.apiService.createRepository(payload)
                _isSuccess.value = true
            } catch (e: Exception) {
                _errorMsg.value = "Error al crear: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Método para ACTUALIZAR (PATCH / PUT) el repositorio
    fun updateRepository(name: String, description: String) {
        val repoActual = _repoToEdit.value ?: return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val payload = RepositoryPayload(name = name, description = description)

                RetrofitClient.apiService.updateRepository(
                    owner = repoActual.owner.login,
                    repo = repoActual.name,
                    repository = payload
                )
                _isSuccess.value = true
            } catch (e: Exception) {
                _errorMsg.value = "Error al actualizar: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Método para limpiar los estados al terminar con éxito
    fun resetSuccess() {
        _isSuccess.value = false
        _repoToEdit.value = null
    }

    fun resetError() {
        _errorMsg.value = null
    }
}