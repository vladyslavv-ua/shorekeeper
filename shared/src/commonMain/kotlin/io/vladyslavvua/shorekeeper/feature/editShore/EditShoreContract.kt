package io.vladyslavvua.shorekeeper.feature.editShore

data class EditShoreState(
    val url: String = "localhost:4200",
    val isCefReady: Boolean = false,
    val isAuthDialogOpen: Boolean = true,

    val username: String = "",
    val password: String = "",
)


sealed class EditShoreIntent {
    data class SetUrl(val url: String) : EditShoreIntent()
    data object RunCef : EditShoreIntent()
    data class SetCredentials(val username: String, val password: String) : EditShoreIntent()
}