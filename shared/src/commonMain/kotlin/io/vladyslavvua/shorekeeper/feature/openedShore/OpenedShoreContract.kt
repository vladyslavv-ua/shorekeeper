package io.vladyslavvua.shorekeeper.feature.openedShore

data class OpenedShoreState(
    val isCefReady: Boolean = false,
    val isAuthDialogOpen: Boolean = false,
    val isDevToolsOpened: Boolean = false
)

sealed class OpenedShoreEffect {
    data object CloseEffect : OpenedShoreEffect()
}

sealed class OpenedShoreIntent {
    data object Close : OpenedShoreIntent()
    data class ConfirmCredentials(val username: String, val password: String) : OpenedShoreIntent()
}