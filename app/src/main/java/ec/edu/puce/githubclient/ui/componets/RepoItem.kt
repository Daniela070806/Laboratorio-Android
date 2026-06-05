package ec.edu.puce.githubclient.ui.componets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ec.edu.puce.githubclient.models.GitHubUser
import ec.edu.puce.githubclient.models.Repository
import ec.edu.puce.githubclient.ui.theme.GithubClientTheme


@Composable

fun RepoItem (
    repository: Repository,
    onEdit: (Repository) -> Unit = {},
    onDelete: (Repository) -> Unit = {}
) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .padding( all= 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = repository.owner.avatarUrl,
                contentDescription = "Imagen de repositorio \"${repository.name}\"",
                modifier= Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(width = 16.dp))
            Column(modifier = Modifier.weight(weight = 1f) ) {
                Text(
                    text = repository.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(height = 4.dp))

                repository.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3
                    )
                }
                repository.language?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }

                Row {
                    IconButton(
                        onClick = { onEdit(repository) } // <--- CRUCIAL: Aquí debes enviarle el 'repository'
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit, // O el ícono que uses para el lápiz
                            contentDescription = "Editar repositorio"
                        )
                    }

                    IconButton(
                        onClick = {
                            onDelete(repository)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar"
                        )
                    }
                }

            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun RepoItemPreview() {
    GithubClientTheme() {
        val repository = Repository(
            id = "123",
            name = "Nombre del repositorio",
            description = "Descripcion del repositorio",
            language = "kotlin",
            owner = GitHubUser(
                id = "123",
                login = "Daniela070806",
                avatarUrl = "https://static.vecteezy.com/system/resources/previews/077/675/681/non_2x/simple-outline-round-user-account-profile-avatar-sign-icon-vector.jpg",

            )
        )
        RepoItem(
            repository
        )
    }

}