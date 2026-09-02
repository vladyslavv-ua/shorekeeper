package io.vladyslavvua.shorekeeper.feature.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.vladyslavvua.shorekeeper.feature.welcome.createShoreDialog.CreateShoreDialogState
import io.vladyslavvua.shorekeeper.feature.welcome.map.toShoreState
import io.vladyslavvua.shorekeeper.room.dao.ShoreDao
import io.vladyslavvua.shorekeeper.room.entity.ShoreTable
import io.vladyslavvua.shorekeeper.room.repo.ShoreRepo
import io.vladyslavvua.shorekeeper.shore.util.initShoreFileStructure
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class WelcomeScreenViewModel(
    private val shoreRepo: ShoreRepo,
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
            val creationResult = initShoreFileStructure(
                shore.toShore(),
                shore.path,
            )
            if (!creationResult) return@launch
            shoreRepo.insertShore(shore.name, shore.path)
            initShoreDb()

            state.update {
                it.copy(isCreateShoreDialogOpen = false)
            }
        }

    }
}