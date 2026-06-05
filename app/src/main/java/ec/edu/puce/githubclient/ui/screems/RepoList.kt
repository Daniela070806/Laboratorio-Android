package ec.edu.puce.githubclient.ui.screems

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.puce.githubclient.ui.componets.RepoItem
import ec.edu.puce.githubclient.ui.theme.GithubClientTheme
import ec.edu.puce.githubclient.viewmodels.RepoFormViewModel
import ec.edu.puce.githubclient.viewmodels.RepoListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoList(
    onNavigateToForm: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RepoListViewModel = viewModel(),
    formViewModel: RepoFormViewModel = viewModel() // <--- Agregado para recibir la instancia compartida del Main
) {
    val repos by viewModel.repos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errMsg by viewModel.errMsg.collectAsState()
    val isDeleteSuccess by viewModel.isDeleteSuccess.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = isDeleteSuccess) {
        if (isDeleteSuccess) {
            snackbarHostState.showSnackbar(
                message = "Repositorio eliminado con éxito",
                actionLabel = "Ok"
            )
            viewModel.resetDeleteSuccess()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    formViewModel.setRepositoryToEdit(null) // <--- Limpia los campos antes de abrir un nuevo formulario
                    onNavigateToForm()
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir repositorio"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            errMsg?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(all = 16.dp)
                )
            }

            if (!isLoading && errMsg == null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(repos.size) { i ->
                        val repo = repos[i]
                        RepoItem(
                            repository = repo,
                            onDelete = {
                                viewModel.deleteRepository(
                                    owner = repo.owner.login,
                                    repo = repo.name
                                )
                            },
                            onEdit = { repoSeleccionado ->
                                // 1. Guardamos el repositorio seleccionado en el ViewModel del Formulario
                                formViewModel.setRepositoryToEdit(repoSeleccionado)
                                // 2. Cambiamos la pantalla a RepoForm en el Main
                                onNavigateToForm()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RepoListPreview() {
    GithubClientTheme {
        RepoList(onNavigateToForm = {})
    }
}