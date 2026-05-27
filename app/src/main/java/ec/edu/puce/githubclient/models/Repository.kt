package ec.edu.puce.githubclient.models

import android.accessibilityservice.GestureDescription
import org.intellij.lang.annotations.Language


data class Repository(
    val id : String,
    val name: String,
    val description: String?,
    val language: String?,
    val owner: GitHubUser
)
