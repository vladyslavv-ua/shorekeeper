package io.vladyslavvua.shorekeeper.feature.editShore

data class EditShoreState(

    val isAuthDialogOpen: Boolean = true,

    val username: String = "",
    val password: String = "",
)


sealed class EditShoreIntent {
    data class SetCredentials(val username: String, val password: String) : EditShoreIntent()
}