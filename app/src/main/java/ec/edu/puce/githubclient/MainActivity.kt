package ec.edu.puce.githubclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.puce.githubclient.ui.screems.RepoForm
import ec.edu.puce.githubclient.ui.screems.RepoList
import ec.edu.puce.githubclient.ui.theme.GithubClientTheme
import ec.edu.puce.githubclient.viewmodels.RepoFormViewModel
import ec.edu.puce.githubclient.viewmodels.RepoListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubClientTheme {
                var currentScreen by remember { mutableStateOf("RepoList") }

                // Inicializamos AMBOS ViewModels aquí arriba para que retengan los estados
                val listViewModel: RepoListViewModel = viewModel()
                val formViewModel: RepoFormViewModel = viewModel() // <--- CRUCIAL

                when (currentScreen) {
                    "RepoList" -> RepoList(
                        onNavigateToForm = { currentScreen = "RepoForm" },
                        viewModel = listViewModel,        // Tu ViewModel de la lista
                        formViewModel = formViewModel     // Pasamos el compartidor del formulario
                    )
                    "RepoForm" -> RepoForm(
                        onBackClick = { currentScreen = "RepoList" },
                        onSaveSuccess = {
                            listViewModel.fetchRepos() // Recarga la lista para ver los cambios
                            currentScreen = "RepoList"
                        },
                        viewModel = formViewModel         // Usamos el mismo compartidor retenido
                    )
                }
            }
        }
    }
}