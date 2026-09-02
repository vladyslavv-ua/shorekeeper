package io.vladyslavvua.shorekeeper.feature.welcome

import io.vladyslavvua.shorekeeper.feature.welcome.createShoreDialog.CreateShoreDialogState
import io.vladyslavvua.shorekeeper.room.entity.ShoreTable

data class WelcomeState(
    val shores: List<ShoreState> = emptyList(),
    val isCreateShoreDialogOpen: Boolean = false,
) {
    data class ShoreState(
        val id: Long,
        val name: String,
        val path: String,
        val selected: Boolean = false
    )
}

sealed class WelcomeIntent {
    data object OpenSettings : WelcomeIntent()
    data object OpenShore : WelcomeIntent()
    data object OpenCreateShoreDialog : WelcomeIntent()
    data object CloseCreateShoreDialog : WelcomeIntent()
    data class CreateShore(val params: CreateShoreDialogState) : WelcomeIntent()
    data class SelectShore(val id: Long) : WelcomeIntent()

    data object OpenAddShoreDialog : WelcomeIntent()

    data object OpenCef: WelcomeIntent()


}

sealed class WelcomeEffect {

    data object OpenSettingsAction : WelcomeEffect()
    data object OpenCef : WelcomeEffect()

    data class OpenShore(val shoreId: Long) : WelcomeEffect()
}