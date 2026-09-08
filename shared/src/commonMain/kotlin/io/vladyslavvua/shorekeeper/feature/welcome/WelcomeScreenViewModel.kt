package io.vladyslavvua.shorekeeper.feature.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.vladyslavvua.shorekeeper.feature.welcome.createShoreDialog.CreateShoreDialogState
import io.vladyslavvua.shorekeeper.feature.welcome.map.toShoreState
import io.vladyslavvua.shorekeeper.room.repo.ShoreRepo
import io.vladyslavvua.shorekeeper.settings.ShorekeeperSettingsManager
import io.vladyslavvua.shorekeeper.shore.util.initShoreFileStructure
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import java.io.File

@KoinViewModel
class WelcomeScreenViewModel(
    private val shoreRepo: ShoreRepo,
    private val settings: ShorekeeperSettingsManager
) : ViewModel() {

    private val _events = Channel<WelcomeEffect>()
    val events = _events.receiveAsFlow()

    val state: StateFlow<WelcomeState>
        field = MutableStateFlow(WelcomeState())

    init {
        initShoreDb()
    }

    private fun initShoreDb() {
        viewModelScope.launch {
            val shores = shoreRepo.getShores()
            state.update { it ->
                it.copy(shores = shores.map { it.toShoreState() })
            }
        }
    }

    fun onIntent(intent: WelcomeIntent) {
        when (intent) {
            is WelcomeIntent.OpenSettings -> openSettings()
            is WelcomeIntent.OpenShore -> openShore()
            is WelcomeIntent.OpenCreateShoreDialog -> openCreateShoreDialog()
            is WelcomeIntent.CloseCreateShoreDialog -> closeCreateShoreDialog()
            is WelcomeIntent.CreateShore -> createShore(intent.params)
            is WelcomeIntent.SelectShore -> selectShore(intent.id)
            is WelcomeIntent.OpenCef -> openCef()
            is WelcomeIntent.OpenEditShore -> openEditShore()
            else -> Unit
        }

    }

    private fun selectShore(id: Long) {
        state.update {
            it.copy(
                shores = it.shores.map { shore ->
                    if (shore.id == id) shore.copy(selected = true) else shore.copy(selected = false)
                }
            )
        }
    }

    private fun openEditShore() {
        val activeShore = state.value.shores.firstOrNull { it.selected }
        if (activeShore == null) return

        _events.trySend(WelcomeEffect.OpenEditShore(activeShore.id))
    }

    private fun openSettings() {
        _events.trySend(WelcomeEffect.OpenSettingsAction)
    }

    private fun openShore() {
        val activeShore = state.value.shores.firstOrNull { it.selected }
        if (activeShore == null) return

        _events.trySend(WelcomeEffect.OpenShore(activeShore.id))


    }

    private fun openCreateShoreDialog() {
        state.update {
            it.copy(isCreateShoreDialogOpen = true)
        }
    }

    private fun openCef() {
        _events.trySend(WelcomeEffect.OpenCef)
    }

    private fun closeCreateShoreDialog() {
        state.update {
            it.copy(isCreateShoreDialogOpen = false)
        }
    }

    private fun createShore(shore: CreateShoreDialogState) {
        viewModelScope.launch {
            val migrator = settings.getMigratorPath(shore.migrator)
            val creationResult = initShoreFileStructure(
                shore.toShore(),
                shore.path,
            )
            val formattedPath = File(shore.path).absolutePath
            migrator.initFileStructure(formattedPath)
            val process = migrator.initProject("${formattedPath}/migrations", shore.connectionString, shore.dbUser, shore.dbPassword)
            process.inheritIO()
            process.start().waitFor()

            if (!creationResult) return@launch
            shoreRepo.insertShore(shore.name, shore.path)
            initShoreDb()

            state.update {
                it.copy(isCreateShoreDialogOpen = false)
            }
        }

    }
}